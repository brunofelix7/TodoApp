package com.brunofelixdev.mytodoapp.data.db

import androidx.paging.PagingSource

interface ItemRepository {
    suspend fun insert(item: Item): OperationResult<Long>
    suspend fun update(item: Item): OperationResult<Unit>
    suspend fun delete(item: Item): OperationResult<Unit>
    suspend fun checkAsDone(item: Item): OperationResult<Unit>
    fun fetchAllOrderByName(): PagingSource<Int, Item>
    fun fetchAllOrderByDueDate(): PagingSource<Int, Item>
}