package com.brunofelixdev.mytodoapp.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.parseToDate(pattern: String = "dd/MM/yyyy"): Date? {
    val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
    return try {
        formatter.parse(this)
    } catch (e: Exception) {
        null
    }
}