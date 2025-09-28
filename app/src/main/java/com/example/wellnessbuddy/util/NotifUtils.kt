package com.example.wellnessbuddy.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wellnessbuddy.R

object NotifUtils {
    const val CHANNEL_HYDRATION = "hydration_channel"

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_HYDRATION,
                context.getString(R.string.app_name) + " Hydration",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mgr.createNotificationChannel(channel)
        }
    }

    fun buildHydrationNotification(context: Context, text: String) =
        NotificationCompat.Builder(context, CHANNEL_HYDRATION)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(text)
            .setAutoCancel(true)
            .build()

    fun notify(context: Context, id: Int, text: String) {
        val notification = buildHydrationNotification(context, text)
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}


