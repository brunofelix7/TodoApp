package com.brunofelixdev.mytodoapp.extension

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