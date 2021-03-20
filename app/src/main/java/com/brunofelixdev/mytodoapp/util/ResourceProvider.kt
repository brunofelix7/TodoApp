package com.brunofelixdev.mytodoapp.util

import android.content.Context
import android.content.res.Resources
import javax.inject.Inject

class ResourceProvider @Inject constructor(private val context: Context) {

    fun getResources(): Resources {
        return this.context.resources
    }
}