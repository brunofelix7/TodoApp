package com.brunofelixdev.mytodoapp.data.db.repository

import android.util.Log
import androidx.paging.PagingSource
import com.brunofelixdev.mytodoapp.data.db.DataResult
import com.brunofelixdev.mytodoapp.data.db.dao.ItemDao
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemRepositoryContract
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val dao: ItemDao
) : ItemRepositoryContract {

    companion object {
        private val TAG: String = ItemRepositoryContract::class.java.simpleName
    }

    override suspend fun insert(item: Item): DataResult<Long> {
        return try {
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

    override fun fetchAll(): PagingSource<Int, Item> = dao.fetchAll()
}