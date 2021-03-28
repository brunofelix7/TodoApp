package com.brunofelixdev.mytodoapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.brunofelixdev.mytodoapp.extension.sendNotification

class ItemWorker (
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_ID = "key-id"
        const val KEY_NAME = "key-name"
        const val KEY_DUE_DATE = "key-due-date"
        private val TAG: String = ItemWorker::class.java.simpleName
    }

    override suspend fun doWork(): Result {
        val id = inputData.getLong(KEY_ID, 0)
        val name = inputData.getString(KEY_NAME) ?: "Unknow name"
        val dueDate = inputData.getString(KEY_DUE_DATE) ?: "Unknow description"

        applicationContext.sendNotification(name, dueDate, id)
        return Result.success()
    }
}