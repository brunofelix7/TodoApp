package com.brunofelixdev.mytodoapp.data.db.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.brunofelixdev.mytodoapp.data.db.OperationResult
import com.brunofelixdev.mytodoapp.data.db.dao.ItemDao
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.data.db.repository.contract.ItemRepositoryContract
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val dao: ItemDao
) : ItemRepositoryContract {

    private val TAG = "MyTag"

    override suspend fun insert(item: Item): OperationResult<Long> {
        return try {
            val result = dao.insert(item)

            if (result > 0) {
                OperationResult.Success(result)
            } else {
                OperationResult.Error("Item '${item.name}' cannot be inserted.")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "An error occurred while entering the data.")
            OperationResult.Error(e.message ?: "An error occurred while entering the data.")
        }
    }

    override fun fetchAll(): LiveData<PagedList<Item>> {
        return dao.fetchAll().toLiveData(pageSize = 50)
    }
}