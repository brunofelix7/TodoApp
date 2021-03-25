package com.brunofelixdev.mytodoapp.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class ItemWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        private val TAG: String = ItemWorker::class.java.simpleName
    }

    override fun doWork(): Result {
        //  TODO: Notify user
        Log.d(TAG, "Check task time....")

        return Result.success()
    }

}