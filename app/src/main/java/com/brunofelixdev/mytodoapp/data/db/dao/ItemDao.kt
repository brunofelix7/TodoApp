package com.brunofelixdev.mytodoapp.data.db.dao

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

    @Query("UPDATE items SET isDone = 1 WHERE id = :id")
    suspend fun checkAsDone(id: Int)

    @Query("SELECT * FROM items WHERE isDone == 0 ORDER BY name ASC")
    suspend fun fetchAllOrderByName(): List<Item>

    @Query("SELECT * FROM items WHERE isDone == 0 ORDER BY datetime(dueDateTime) ASC")
    suspend fun fetchAllOrderByDueDate(): List<Item>

}