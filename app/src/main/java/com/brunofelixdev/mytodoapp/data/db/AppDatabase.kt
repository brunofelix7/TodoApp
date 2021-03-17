package com.brunofelixdev.mytodoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brunofelixdev.mytodoapp.data.db.dao.ItemDao
import com.brunofelixdev.mytodoapp.data.db.entity.Item

@Database(
    entities = [Item::class],
    version = DbSchema.DB_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

}