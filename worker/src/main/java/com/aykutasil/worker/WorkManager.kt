/* Author - Aykut Asil(@aykuttasil) */
package com.aykutasil.worker

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkerManager {

    const val DATA_KEY_REQUEST_JSON = "request_json"

    fun scheduleLocationWorker(
      tag: String,
      data: Data,
      networkType: NetworkType
    ) {
        val constraints = Constraints.Builder().setRequiredNetworkType(networkType).build()

        val work = OneTimeWorkRequestBuilder<LocationWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            .addTag(tag)
            .build()

        WorkManager.getInstance().enqueue(work)
    }
}
