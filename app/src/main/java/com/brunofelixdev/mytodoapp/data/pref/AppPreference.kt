package com.brunofelixdev.mytodoapp.data.pref

import android.app.Activity
import android.content.Context
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.util.Constants

fun saveFilter(activity: Activity?, filter: String) {
    val pref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    with (pref.edit()) {
        putString(activity.resources?.getString(R.string.pref_key_filter), filter)
        apply()
    }
}

fun getFilter(activity: Activity?): String? {
    val pref = activity?.getPreferences(Context.MODE_PRIVATE)
    return pref?.getString(activity.resources.getString(R.string.pref_key_filter), Constants.SORT_BY_NAME)
}