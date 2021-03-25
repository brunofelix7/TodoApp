package com.brunofelixdev.mytodoapp

import android.app.Application
import com.brunofelixdev.mytodoapp.extension.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
}