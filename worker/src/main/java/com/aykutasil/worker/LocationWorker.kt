package com.aykutasil.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class LocationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        try {
            return Result.success()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return Result.retry()
        }
    }
}