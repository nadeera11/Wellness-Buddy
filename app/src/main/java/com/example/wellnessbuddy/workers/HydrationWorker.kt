package com.example.wellnessbuddy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wellnessbuddy.data.PrefsStore
import com.example.wellnessbuddy.util.NotifUtils
import java.util.Calendar

class HydrationWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val store = PrefsStore(applicationContext)
        // Respect quiet hours window
        val start = store.getReminderStartMinutes()
        val end = store.getReminderEndMinutes()
        val nowCal = Calendar.getInstance()
        val nowMin = nowCal.get(Calendar.HOUR_OF_DAY) * 60 + nowCal.get(Calendar.MINUTE)
        val inWindow = if (end >= start) {
            nowMin in start..end
        } else {
            // Over midnight
            nowMin >= start || nowMin <= end
        }
        if (inWindow) {
            NotifUtils.notify(applicationContext, 1001, "Time to drink water")
        }
        return Result.success()
    }
}


