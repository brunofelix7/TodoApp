package com.brunofelixdev.mytodoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemContract,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Initial)
    val uiStateFlow: StateFlow<UiState> get() = _uiStateFlow

    val itemsList = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
        )
    ) {
        repository.fetchAll()
    }.flow.cachedIn(viewModelScope)

    fun insertItem() {

    }

    sealed class UiState {
        object Initial: UiState()
        object Loading: UiState()
        class Success(val items: LiveData<PagedList<Item>>): UiState()
        class Error(val errorMessage: String): UiState()
    }
}