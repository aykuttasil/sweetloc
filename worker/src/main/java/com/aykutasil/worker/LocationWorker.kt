package com.aykutasil.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class LocationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            return Result.success()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return Result.retry()
        }
    }

}