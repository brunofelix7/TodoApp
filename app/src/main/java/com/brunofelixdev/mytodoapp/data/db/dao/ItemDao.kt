package com.brunofelixdev.mytodoapp.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.brunofelixdev.mytodoapp.data.db.DbSchema
import com.brunofelixdev.mytodoapp.data.db.entity.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("UPDATE items SET isDone = 1 WHERE id = :id")
    suspend fun checkAsDone(id: Int)

    @Query(DbSchema.QUERY_ORDER_BY_DATETIME)
    fun fetchAll(): PagingSource<Int, Item>

}