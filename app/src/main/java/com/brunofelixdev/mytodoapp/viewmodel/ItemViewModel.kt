package com.brunofelixdev.mytodoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.DataResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemRepositoryContract
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

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Initial)
    val uiStateFlow: StateFlow<UiState> get() = _uiStateFlow

    val itemsList = Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
        repository.fetchAll()
    }.flow.cachedIn(viewModelScope)

    val formErrors = mutableMapOf<String, String>()

    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_DUE_DATE = "dueDate"
    }

    fun insertItem(item: Item) {
        viewModelScope.launch(defaultDispatcher) {
            formValidation(item)

            if (formErrors.isNotEmpty()) {
                _uiStateFlow.value = UiState.Error(
                    resourcesProvider.getResources().getString(R.string.msg_fields_required)
                )
            } else {
                _uiStateFlow.value = UiState.Loading

                when (repository.insert(item)) {
                    is DataResult.Error -> {
                        _uiStateFlow.value =
                            UiState.Error(
                                resourcesProvider.getResources().getString(R.string.msg_fail_add)
                            )
                    }
                    is DataResult.Success -> {
                        _uiStateFlow.value =
                            UiState.Success(
                                resourcesProvider.getResources().getString(R.string.msg_success_add)
                            )
                    }
                }
            }
        }
    }

    fun updateItem() {

    }

    fun deleteItem() {

    }

    private fun formValidation(item: Item) {
        formErrors.clear()

        if (item.name.isEmpty()) {
            formErrors[FIELD_NAME] = resourcesProvider.getResources().getString(R.string.msg_required_name)
        }
        if (item.dueDate.isEmpty() || item.dueDate.length < 10) {
            formErrors[FIELD_DUE_DATE] = resourcesProvider.getResources().getString(R.string.msg_required_due_date)
        }
    }

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        class Success(val successMessage: String) : UiState()
        class Error(val errorMessage: String) : UiState()
    }
}