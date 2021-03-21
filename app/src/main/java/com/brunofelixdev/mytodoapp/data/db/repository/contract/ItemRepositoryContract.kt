package com.brunofelixdev.mytodoapp.data.db.repository.contract

import androidx.paging.PagingSource
import com.brunofelixdev.mytodoapp.data.db.DataResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item

interface ItemRepositoryContract {
    suspend fun insert(item: Item): DataResult<Long>
    suspend fun checkAsDone(id: Int)
    fun fetchAll(): PagingSource<Int, Item>
}