package com.brunofelixdev.mytodoapp.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.brunofelixdev.mytodoapp.data.db.entity.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items")
    fun fetchAll(): PagingSource<Int, Item>

}