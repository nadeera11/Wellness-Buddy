package com.example.wellnessbuddy.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.wellnessbuddy.MainActivity
import com.example.wellnessbuddy.R
import com.example.wellnessbuddy.data.PrefsStore
import com.example.wellnessbuddy.data.model.Habit
import com.example.wellnessbuddy.data.model.MoodEntry
import java.text.SimpleDateFormat
import java.util.*

class WellnessWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widget instances
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        // Handle custom refresh action
        if (intent.action == ACTION_REFRESH_WIDGET) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, WellnessWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }

    companion object {
        const val ACTION_REFRESH_WIDGET = "com.example.wellnessbuddy.REFRESH_WIDGET"
        
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Get data from SharedPreferences
            val prefsStore = PrefsStore(context)
            val habits = prefsStore.getHabits()
            val moods = prefsStore.getMoods()
            val waterTarget = prefsStore.getDailyWaterTargetMl()
            val currentWater = prefsStore.getCurrentWaterIntake()
            
            // Calculate today's progress
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            
            // Habits progress
            val completedHabits = habits.count { habit ->
                val progress = habit.progressByYmd[today] ?: 0
                progress >= habit.dailyTarget
            }
            val totalHabits = habits.size
            val habitsProgress = if (totalHabits == 0) 0 else (completedHabits * 100 / totalHabits)
            
            // Mood status
            val todayMoods = moods.filter { mood ->
                val moodDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(mood.timestamp))
                moodDate == today
            }
            val latestMood = todayMoods.maxByOrNull { it.timestamp }
            
            // Water progress
            val waterProgress = if (waterTarget > 0) ((currentWater.toFloat() / waterTarget) * 100).toInt().coerceIn(0, 100) else 0
            
            // Create RemoteViews
            val views = RemoteViews(context.packageName, R.layout.widget_wellness_layout)
            
            // Update UI elements
            views.setTextViewText(R.id.widget_date, SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date()))
            views.setTextViewText(R.id.widget_habits_progress, "$completedHabits/$totalHabits")
            views.setTextViewText(R.id.widget_water_progress, "${currentWater}/${waterTarget}ml")
            views.setTextViewText(R.id.widget_mood_count, "(${todayMoods.size})")
            
            // Set mood emoji
            val moodEmoji = latestMood?.emoji ?: "😐"
            views.setTextViewText(R.id.widget_mood_emoji, moodEmoji)
            
            // Update progress percentages
            views.setTextViewText(R.id.widget_habits_percentage, "${habitsProgress}%")
            views.setTextViewText(R.id.widget_water_percentage, "${waterProgress}%")
            
            // Set click intent to open app
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_layout_root, pendingIntent)
            
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        
        
        fun refreshAllWidgets(context: Context) {
            val intent = Intent(context, WellnessWidgetProvider::class.java)
            intent.action = ACTION_REFRESH_WIDGET
            context.sendBroadcast(intent)
        }
    }
}
