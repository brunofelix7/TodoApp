package com.brunofelixdev.mytodoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemContract,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Initial)
    val uiStateFlow: StateFlow<UiState> get() = _uiStateFlow

    private val _itemsList = Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
        repository.fetchAll()
    }.flow.cachedIn(viewModelScope)

    fun insertItem() {

    }

    fun fetchItems() {
        viewModelScope.launch(defaultDispatcher) {
            _uiStateFlow.value = UiState.Loading

            _itemsList.collect {
                _uiStateFlow.value = UiState.Success(it)
            }
        }
    }

    sealed class UiState {
        object Initial: UiState()
        object Loading: UiState()
        class Success(val items: PagingData<Item>): UiState()
        class Error(val errorMessage: String): UiState()
    }
}