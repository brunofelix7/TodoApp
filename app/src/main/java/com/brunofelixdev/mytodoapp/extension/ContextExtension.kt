package com.brunofelixdev.mytodoapp.extension

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.ui.activity.MainActivity
import com.brunofelixdev.mytodoapp.util.Constants
import com.brunofelixdev.mytodoapp.worker.ItemWorker
import java.util.concurrent.TimeUnit

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Context.sendNotification(title: String, description: String, notificationId: Long) {
    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

    val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_small_icon)
        .setContentTitle(title)
        .setColor(ContextCompat.getColor(this, R.color.purple_500))
        .setContentText(description)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(this)) {
        notify(notificationId.toInt(), builder.build())
    }
}

fun Context.createWork(item: Item, notificationId: Long) {
    val inputData = Data.Builder().apply {
        putLong(ItemWorker.KEY_ID, notificationId)
        putString(ItemWorker.KEY_NAME, item.name)
        item.apply {
            putString(ItemWorker.KEY_DUE_DATE, "${dueDateTime.getDueDateView()} ${dueDateTime.getDueTime()}")
        }
    }

    val notificationWork = OneTimeWorkRequest.Builder(ItemWorker::class.java)
        .setInitialDelay(item.workDuration.toLong(), TimeUnit.MINUTES)
        .setInputData(inputData.build())
        .addTag(item.workTag)
        .build()
    WorkManager.getInstance(this).enqueue(notificationWork)
}

fun Context.cancelWork(workTag: String) {
    WorkManager.getInstance(this).cancelAllWorkByTag(workTag)
}