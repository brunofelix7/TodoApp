package com.brunofelixdev.mytodoapp.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.brunofelixdev.mytodoapp.util.Constants
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime
import org.joda.time.Duration
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
@Entity(tableName = DbSchema.TB_ITEMS)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String = "",
    var isDone: Boolean = false,
    var dueDateTime: String = "",
    var workTag: String = "",
    var workDuration: Int = 0
) : Parcelable {

    fun String.getDueTime(): String {
        val date = this.parseToDate(Constants.PATTERN_YYYY_MM_DD_HH_MM)
        return date?.parseToString(Constants.PATTERN_HH_MM) ?: "error"
    }

    fun String.getDueDate(): String {
        val date = this.parseToDate(Constants.PATTERN_YYYY_MM_DD_HH_MM)
        return date?.parseToString(Constants.PATTERN_MM_DD_YYYY) ?: "error"
    }

    fun String.getDueDateView(): String {
        val date = this.parseToDate(Constants.PATTERN_YYYY_MM_DD_HH_MM)
        return date?.parseToString(Constants.PATTERN_EEE_MMM_DD_YYY) ?: "error"
    }

    fun String.toDbFormat(): String {
        val date = this.parseToDate(Constants.PATTERN_MM_DD_YYYY_HH_MM)
        return date?.parseToString(Constants.PATTERN_YYYY_MM_DD_HH_MM) ?: "error"
    }

    fun String.getDurationBetweenDates() : Int {
        val date = this.parseToDate(Constants.PATTERN_YYYY_MM_DD_HH_MM)
        val dateTo = date?.parseToString(Constants.PATTERN_MM_DD_YYYY_HH_MM)
        val from = DateTime.now()
        val to = DateTime(dateTo?.parseToDate(Constants.PATTERN_MM_DD_YYYY_HH_MM))
        val duration = Duration(from, to)
        return duration.standardMinutes.toInt()
    }

    private fun Date.parseToString(pattern: String): String? {
        return try {
            val df = SimpleDateFormat(pattern, Locale.ENGLISH)
            return df.format(this)
        } catch (e: Exception) {
            null
        }
    }

    private fun String.parseToDate(pattern: String): Date? {
        val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
        return try {
            formatter.parse(this)
        } catch (e: Exception) {
            null
        }
    }
}