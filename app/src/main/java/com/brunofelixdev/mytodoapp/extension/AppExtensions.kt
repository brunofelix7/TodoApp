package com.brunofelixdev.mytodoapp.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.parseToString(pattern: String): String? {
    return try {
        val df = SimpleDateFormat(pattern, Locale.ENGLISH)
        return df.format(this)
    } catch (e: Exception) {
        null
    }
}

fun String.parseToDate(pattern: String): Date? {
    val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
    return try {
        formatter.parse(this)
    } catch (e: Exception) {
        null
    }
}