package com.example.automatedwhatsappmessage.worker

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

object WorkerCreator {

    private const val TAG = "Work Creator"

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(false)
        .setRequiresDeviceIdle(false)
        .build()

    fun setupOnetimeBFUWorker(context: Context) {
        Log.i(TAG, "setupOnetimeBFUWorker: Creating onetime Block Floor Unit Worker")

        val myWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UploadCallStatusWorker>()
                .addTag(UploadCallStatusWorker.WORK_NAME)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    120L,
                    TimeUnit.SECONDS
                )
                .setInitialDelay(10_000L, TimeUnit.MILLISECONDS)
                .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            UploadCallStatusWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            myWorkRequest
        )
        Log.i(TAG, "setupOnetimeBFUWorker: Created onetime Block Floor Unit Worker")
    }

}