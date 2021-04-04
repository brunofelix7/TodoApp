package com.brunofelixdev.mytodoapp.data.db.repository

import android.util.Log
import androidx.paging.PagingSource
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.dao.ItemDao
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemRepositoryContract
import com.brunofelixdev.mytodoapp.extension.getDurationBetweenDates
import com.brunofelixdev.mytodoapp.extension.parseToDate
import com.brunofelixdev.mytodoapp.extension.parseToString
import java.util.*
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val dao: ItemDao
) : ItemRepositoryContract {

    companion object {
        private val TAG: String = ItemRepositoryContract::class.java.simpleName
    }

    override suspend fun insert(item: Item): OperationResult<Long> {
        return try {
            val dueDate = item.dueDate
            val dueDateResult = item.dueDate.parseToDate()

            item.dueDateTime = "$dueDate ${item.dueTime}"
                .parseToDate("MM-dd-yyyy HH:mm")
                ?.parseToString("yyyy-MM-dd HH:mm")!!
            item.dueDate = dueDateResult?.parseToString() ?: dueDate
            item.workTag = "tag-${UUID.randomUUID()}"
            item.workDuration = getDurationBetweenDates("$dueDate ${item.dueTime}")

            val result = dao.insert(item)

            if (result > 0) {
                OperationResult.Success(result)
            } else {
                OperationResult.Error("Oops! Try again.")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Oops! Try again.")
            OperationResult.Error(e.message ?: "Oops! Try again.")
        }
    }

    override suspend fun update(item: Item): OperationResult<Unit> {
        return try {
            val dueDate = item.dueDate
            val dueDateResult = item.dueDate.parseToDate()

            item.dueDateTime = "$dueDate ${item.dueTime}"
                .parseToDate("MM-dd-yyyy HH:mm")
                ?.parseToString("yyyy-MM-dd HH:mm")!!
            item.dueDate = dueDateResult?.parseToString() ?: dueDate
            item.workDuration = getDurationBetweenDates("$dueDate ${item.dueTime}")

            dao.update(item)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Oops! Try again.")
            OperationResult.Error(e.message ?: "Oops! Try again.")
        }
    }

    override suspend fun delete(item: Item): OperationResult<Unit> {
        return try {
            dao.delete(item)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Oops! Try again.")
            OperationResult.Error(e.message ?: "Oops! Try again.")
        }
    }

    override suspend fun checkAsDone(item: Item): OperationResult<Unit> {
        return try {
            dao.checkAsDone(item.id)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Oops! Try again.")
            OperationResult.Error(e.message ?: "Oops! Try again.")
        }
    }

    override fun fetchAllOrderByName(): PagingSource<Int, Item> = dao.fetchAllOrderByName()

    override fun fetchAllOrderByDueDate(): PagingSource<Int, Item> = dao.fetchAllOrderByDueDate()

}