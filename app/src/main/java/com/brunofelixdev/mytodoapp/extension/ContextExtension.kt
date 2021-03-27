package com.brunofelixdev.mytodoapp.extension

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.ui.activity.MainActivity
import com.brunofelixdev.mytodoapp.util.Constants
import com.brunofelixdev.mytodoapp.worker.ItemNotifyWorker
import java.util.concurrent.TimeUnit

fun Context.sendNotification(title: String, description: String, notificationId: Int) {
    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

    //  val bitmapLargeIcon = BitmapFactory.decodeResource(resources, R.mipmap.large_icon)

    val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_small_icon)
        .setContentTitle(title)
        .setColor(ContextCompat.getColor(this, R.color.purple_500))
        .setContentText(description)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(this)) {
        notify(notificationId, builder.build())
    }
}

fun Context.createWork(workTag: String, workId: Int, duration: Long) {
    val inputData = Data.Builder().putInt(workTag, workId).build()
    val notificationWork = OneTimeWorkRequest.Builder(ItemNotifyWorker::class.java)
        .setInitialDelay(duration, TimeUnit.MINUTES)
        .setInputData(inputData)
        .build()
    WorkManager.getInstance(this).enqueue(notificationWork)
}

fun Context.cancelWork(workTag: String) {
    WorkManager.getInstance(this).cancelAllWorkByTag(workTag)
}