package com.brunofelixdev.mytodoapp.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.brunofelixdev.mytodoapp.extension.sendNotification

class ItemNotifyWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        private val TAG: String = ItemNotifyWorker::class.java.simpleName
    }

    override fun doWork(): Result {
        Log.d(TAG, "ID=${workerParams.id}")
        Log.d(TAG, "id=${id}")
        Log.d(TAG, "inputData=${inputData.keyValueMap.values}")
        Log.d(TAG, "inputData2=${inputData.keyValueMap.keys}")

        applicationContext.sendNotification("Send from worker", "It'work!!!", 0)
        return Result.success()
    }
}