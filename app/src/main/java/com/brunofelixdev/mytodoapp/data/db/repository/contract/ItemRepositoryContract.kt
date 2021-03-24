package com.brunofelixdev.mytodoapp.data.db.repository.contract

import androidx.paging.PagingSource
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item

interface ItemRepositoryContract {
    suspend fun insert(item: Item): OperationResult<Long>
    suspend fun update(item: Item): OperationResult<Unit>
    suspend fun delete(item: Item): OperationResult<Unit>
    suspend fun checkAsDone(id: Int): OperationResult<Unit>
    fun fetchAll(): PagingSource<Int, Item>
}