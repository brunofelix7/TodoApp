package com.brunofelixdev.mytodoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemRepositoryContract,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _items = MutableLiveData<PagedList<Item>>()
    val items: LiveData<PagedList<Item>> = repository.fetchAll()

    fun fetchItems() {
        viewModelScope.launch(defaultDispatcher) {

        }
    }

    sealed class UiState {
        object Initial: UiState()
        object Loading: UiState()
        class Success(val items: PagedList<Item>): UiState()
        class Error(val errorMessage: String): UiState()
    }
}