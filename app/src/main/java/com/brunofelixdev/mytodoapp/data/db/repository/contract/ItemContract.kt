package com.brunofelixdev.mytodoapp.data.db.repository.contract

import androidx.paging.PagingSource
import com.brunofelixdev.mytodoapp.data.db.DataResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item

interface ItemContract {
    fun insert(item: Item): DataResult<Long>
    fun fetchAll(): PagingSource<Int, Item>
}