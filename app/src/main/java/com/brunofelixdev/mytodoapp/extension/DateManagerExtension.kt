package com.brunofelixdev.mytodoapp.extension

import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat
import java.util.*

fun Date.parseToString(pattern: String = "EEE, MMM dd, yyyy"): String? {
    return try {
        val df = SimpleDateFormat(pattern, Locale.ENGLISH)
        return df.format(this)
    } catch (e: Exception) {
        null
    }
}

fun String.parseToDate(pattern: String = "MM-dd-yyyy"): Date? {
    val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
    return try {
        formatter.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String.getDateMarker() = this.parseToDate("HH:mm")?.parseToString("HH:mm a")


fun getDurationBetweenDates(dueDateTime: String) : Int {
    val from = DateTime.now()
    val to = DateTime(dueDateTime.parseToDate(pattern = "MM-dd-yyyy HH:mm"))
    val duration = Duration(from, to)
    return duration.standardMinutes.toInt()
}