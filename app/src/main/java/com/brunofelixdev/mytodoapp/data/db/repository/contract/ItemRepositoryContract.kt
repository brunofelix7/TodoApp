package com.brunofelixdev.mytodoapp.data.db.repository.contract

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item

interface ItemRepositoryContract {
    suspend fun insert(item: Item): OperationResult<Long>
    fun fetchAll(): LiveData<PagedList<Item>>
}