package com.example.wellnessbuddy.workers

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wellnessbuddy.data.PrefsStore
import java.util.concurrent.TimeUnit

object HydrationScheduler {
    private const val UNIQUE_NAME = "hydration_periodic"

    fun schedule(context: Context) {
        val store = PrefsStore(context)
        val interval = store.getReminderIntervalMinutes().toLong().coerceAtLeast(15L)
        val request = PeriodicWorkRequestBuilder<HydrationWorker>(interval, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(UNIQUE_NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_NAME)
    }
}


