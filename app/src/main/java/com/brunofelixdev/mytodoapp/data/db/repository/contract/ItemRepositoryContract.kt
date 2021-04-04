package com.brunofelixdev.mytodoapp.data.db.repository.contract

import androidx.paging.PagingSource
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item

interface ItemRepositoryContract {
    suspend fun insert(item: Item): OperationResult<Long>
    suspend fun update(item: Item): OperationResult<Unit>
    suspend fun delete(item: Item): OperationResult<Unit>
    suspend fun checkAsDone(item: Item): OperationResult<Unit>
    fun fetchAllOrderByName(): PagingSource<Int, Item>
    fun fetchAllOrderByDueDate(): PagingSource<Int, Item>
}