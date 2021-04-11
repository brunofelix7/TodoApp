package com.brunofelixdev.mytodoapp.data.db.repository.contract

import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.entity.Item

interface ItemRepositoryContract {

    suspend fun insert(item: Item): OperationResult<Long>

    suspend fun update(item: Item): OperationResult<Unit>

    suspend fun delete(item: Item): OperationResult<Unit>

    suspend fun checkAsDone(item: Item): OperationResult<Unit>

    suspend fun fetchAllOrderByName(): List<Item>

    suspend fun fetchAllOrderByDueDate(): List<Item>

}