package com.brunofelixdev.mytodoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemRepositoryContract
import com.brunofelixdev.mytodoapp.extension.cancelWork
import com.brunofelixdev.mytodoapp.extension.createWork
import com.brunofelixdev.mytodoapp.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val resourcesProvider: ResourceProvider,
    private val repository: ItemRepositoryContract,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _items = MutableStateFlow<UiState>(UiState.Initial)
    val items: StateFlow<UiState> get() = _items

    val formErrors = mutableMapOf<String, String>()

    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_DUE_DATE = "dueDate"
        const val FIELD_DUE_TIME = "dueTime"
    }

    fun fetchAllOrderByName() {
        _items.value = UiState.Loading

        viewModelScope.launch(defaultDispatcher) {
            _items.value = UiState.Success(repository.fetchAllOrderByName(), null)
        }
    }

    fun fetchAllOrderByDueDate() {
        _items.value = UiState.Loading

        viewModelScope.launch(defaultDispatcher) {
            _items.value = UiState.Success(repository.fetchAllOrderByDueDate(), null)
        }
    }

    fun insertItem(item: Item) {
        _items.value = UiState.Loading

        viewModelScope.launch(defaultDispatcher) {
            formValidation(item)

            if (formErrors.isNotEmpty()) {
                _items.value = UiState.Error(
                    resourcesProvider.getResources().getString(R.string.msg_fields_required)
                )
            } else {
                when (val result = repository.insert(item)) {
                    is OperationResult.Error -> {
                        _items.value = UiState.Error(result.message!!)
                    }
                    is OperationResult.Success -> {
                        resourcesProvider.getApplicationContext().createWork(item, result.data!!)

                        _items.value = UiState.Success(null,
                                resourcesProvider.getResources().getString(R.string.msg_success_add))
                    }
                }
            }
        }
    }

    fun checkItemAsDone(item: Item) {
        _items.value = UiState.Loading

        viewModelScope.launch(defaultDispatcher) {
            when (val result = repository.checkAsDone(item)) {
                is OperationResult.Success -> {
                    resourcesProvider.getApplicationContext().cancelWork(item.workTag)

                    _items.value = UiState.Success(null,
                        resourcesProvider.getResources().getString(R.string.msg_success_check_as_done))
                }
                is OperationResult.Error -> {
                    _items.value = UiState.Error(result.message!!)
                }
            }
        }
    }

    fun updateItem(item: Item) {
        _items.value = UiState.Loading

        viewModelScope.launch(defaultDispatcher) {
            when (val result = repository.update(item)) {
                is OperationResult.Success -> {
                    resourcesProvider.getApplicationContext().cancelWork(item.workTag)
                    resourcesProvider.getApplicationContext().createWork(item, item.id.toLong())

                    _items.value = UiState.Success(null,
                        resourcesProvider.getResources().getString(R.string.msg_success_update))
                }
                is OperationResult.Error -> {
                    _items.value = UiState.Error(result.message!!)
                }
            }
        }
    }

    fun deleteItem(item: Item) {
        _items.value = UiState.Loading

        viewModelScope.launch(defaultDispatcher) {
            when (val result = repository.delete(item)) {
                is OperationResult.Success -> {
                    resourcesProvider.getApplicationContext().cancelWork(item.workTag)

                    _items.value = UiState.Success(null,
                        resourcesProvider.getResources().getString(R.string.msg_success_delete))
                }
                is OperationResult.Error -> {
                    _items.value = UiState.Error(result.message!!)
                }
            }
        }
    }

    private fun formValidation(item: Item) {
        formErrors.clear()

        if (item.name.isEmpty()) {
            formErrors[FIELD_NAME] =
                resourcesProvider.getResources().getString(R.string.msg_required_name)
        }
        if (item.dueDateTime.isEmpty() || item.dueDateTime == "error") {
            formErrors[FIELD_DUE_DATE] =
                resourcesProvider.getResources().getString(R.string.msg_required_due_date)
            formErrors[FIELD_DUE_TIME] =
                resourcesProvider.getResources().getString(R.string.msg_required_due_time)
        }
    }

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        class Success(val itemsList: List<Item>?, val message: String?) : UiState()
        class Error(val errorMessage: String) : UiState()
    }
}