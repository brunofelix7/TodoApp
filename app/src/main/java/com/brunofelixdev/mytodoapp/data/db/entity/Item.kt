package com.brunofelixdev.mytodoapp.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.brunofelixdev.mytodoapp.data.db.DbSchema
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DbSchema.TB_ITEMS)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val isDone: Boolean,
    val isImportant: Boolean,
    val dueDate: String
) : Parcelable