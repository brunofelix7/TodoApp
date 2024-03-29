package com.brunofelixdev.mytodoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.Item
import com.brunofelixdev.mytodoapp.data.db.ItemRepository
import com.brunofelixdev.mytodoapp.data.pref.getItemsFilter
import com.brunofelixdev.mytodoapp.extension.cancelWork
import com.brunofelixdev.mytodoapp.extension.createWork
import com.brunofelixdev.mytodoapp.util.Constants
import com.brunofelixdev.mytodoapp.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val resProvider: ResourceProvider,
    private val repository: ItemRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Initial)
    val uiStateFlow: StateFlow<UiState> get() = _uiStateFlow

    val itemsList = Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
        if (getItemsFilter(resProvider.getApplicationContext()) == Constants.SORT_BY_NAME) {
            repository.fetchAllOrderByName()
        } else {
            repository.fetchAllOrderByDueDate()
        }
    }.flow

    val formErrors = mutableMapOf<String, String>()

    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_DUE_DATE = "dueDate"
        const val FIELD_DUE_TIME = "dueTime"
    }

    fun insertItem(item: Item) {
        _uiStateFlow.value = UiState.Loading

        viewModelScope.launch(dispatcher) {
            formValidation(item)

            if (formErrors.isNotEmpty()) {
                _uiStateFlow.value = UiState.Error(
                    resProvider.getResources().getString(R.string.msg_fields_required)
                )
            } else {
                when (val result = repository.insert(item)) {
                    is OperationResult.Error -> {
                        _uiStateFlow.value = UiState.Error(result.message!!)
                    }
                    is OperationResult.Success -> {
                        resProvider.getApplicationContext().createWork(item, result.data!!)

                        _uiStateFlow.value =
                            UiState.Success(
                                resProvider.getResources().getString(R.string.msg_success_add)
                            )
                    }
                }
            }
        }
    }

    fun checkItemAsDone(item: Item) {
        _uiStateFlow.value = UiState.Loading

        viewModelScope.launch(dispatcher) {
            when (val result = repository.checkAsDone(item)) {
                is OperationResult.Success -> {
                    resProvider.getApplicationContext().cancelWork(item.workTag)

                    _uiStateFlow.value = UiState.Success(
                        resProvider.getResources().getString(R.string.msg_success_check_as_done)
                    )
                }
                is OperationResult.Error -> {
                    _uiStateFlow.value = UiState.Error(result.message!!)
                }
            }
        }
    }

    fun updateItem(item: Item) {
        _uiStateFlow.value = UiState.Loading

        viewModelScope.launch(dispatcher) {
            when (val result = repository.update(item)) {
                is OperationResult.Success -> {
                    resProvider.getApplicationContext().cancelWork(item.workTag)
                    resProvider.getApplicationContext().createWork(item, item.id.toLong())

                    _uiStateFlow.value = UiState.Success(
                        resProvider.getResources().getString(R.string.msg_success_update)
                    )
                }
                is OperationResult.Error -> {
                    _uiStateFlow.value = UiState.Error(result.message!!)
                }
            }
        }
    }

    fun deleteItem(item: Item) {
        _uiStateFlow.value = UiState.Loading

        viewModelScope.launch(dispatcher) {
            when (val result = repository.delete(item)) {
                is OperationResult.Success -> {
                    resProvider.getApplicationContext().cancelWork(item.workTag)

                    _uiStateFlow.value = UiState.Success(
                        resProvider.getResources().getString(R.string.msg_success_delete)
                    )
                }
                is OperationResult.Error -> {
                    _uiStateFlow.value = UiState.Error(result.message!!)
                }
            }
        }
    }

    private fun formValidation(item: Item) {
        formErrors.clear()

        if (item.name.isEmpty()) {
            formErrors[FIELD_NAME] =
                resProvider.getResources().getString(R.string.msg_required_name)
        }
        if (item.dueDateTime.isEmpty() || item.dueDateTime == "error") {
            formErrors[FIELD_DUE_DATE] =
                resProvider.getResources().getString(R.string.msg_required_due_date)
            formErrors[FIELD_DUE_TIME] =
                resProvider.getResources().getString(R.string.msg_required_due_time)
        }
    }

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        class Success(val successMessage: String) : UiState()
        class Error(val errorMessage: String) : UiState()
    }
}