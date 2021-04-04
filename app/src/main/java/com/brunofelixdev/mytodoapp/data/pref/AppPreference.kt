package com.brunofelixdev.mytodoapp.data.pref

import android.content.Context
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.util.Constants

fun saveFilterInPreferences(context: Context, filter: String) {
    val prefName = context.resources.getString(R.string.pref_name)
    val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    with (pref.edit()) {
        putString(context.resources.getString(R.string.pref_key_filter), filter)
        apply()
    }
}

fun getFilterFromPreferences(context: Context): String? {
    val prefName = context.resources.getString(R.string.pref_name)
    val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    return pref.getString(context.resources.getString(R.string.pref_key_filter), Constants.SORT_BY_NAME)
}