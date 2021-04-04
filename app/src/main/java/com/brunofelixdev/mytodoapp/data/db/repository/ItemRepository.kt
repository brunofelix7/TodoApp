package com.brunofelixdev.mytodoapp.data.db.repository

import android.util.Log
import androidx.paging.PagingSource
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.dao.ItemDao
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemRepositoryContract
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
            item.workTag = "tag-${UUID.randomUUID()}"
            item.apply {
                workDuration = dueDateTime.getDurationBetweenDates()
            }

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
            item.apply {
                workDuration = dueDateTime.getDurationBetweenDates()
            }

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