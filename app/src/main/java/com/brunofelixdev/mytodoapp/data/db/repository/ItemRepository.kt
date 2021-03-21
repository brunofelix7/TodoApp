package com.brunofelixdev.mytodoapp.data.db.repository

import android.util.Log
import androidx.paging.PagingSource
import com.brunofelixdev.mytodoapp.data.db.DataResult
import com.brunofelixdev.mytodoapp.data.db.dao.ItemDao
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemRepositoryContract
import com.brunofelixdev.mytodoapp.extension.parseToDate
import com.brunofelixdev.mytodoapp.extension.parseToString
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val dao: ItemDao
) : ItemRepositoryContract {

    companion object {
        private val TAG: String = ItemRepositoryContract::class.java.simpleName
    }

    override suspend fun insert(item: Item): DataResult<Long> {
        return try {
            val dueDate = item.dueDate
            val date = item.dueDate.parseToDate()
            item.dueDate = date?.parseToString() ?: dueDate

            val result = dao.insert(item)

            if (result > 0) {
                DataResult.Success(result)
            } else {
                DataResult.Error("Oops! Try again.")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "An error occurred while entering the data.")
            DataResult.Error(e.message ?: "An error occurred while entering the data.")
        }
    }

    override suspend fun checkAsDone(id: Int) = dao.checkAsDone(id)

    override fun fetchAll(): PagingSource<Int, Item> = dao.fetchAll()
}