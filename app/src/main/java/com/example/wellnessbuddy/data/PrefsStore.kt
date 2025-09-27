package com.example.wellnessbuddy.data

import android.content.Context
import android.content.SharedPreferences
import com.example.wellnessbuddy.data.model.Habit
import com.example.wellnessbuddy.data.model.MoodEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Simple JSON-backed store using SharedPreferences.
 * It persists habits, mood entries, and hydration settings without a database.
 */
class PrefsStore(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // region Habits
    fun getHabits(): MutableList<Habit> {
        val json = prefs.getString(KEY_HABITS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Habit>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun saveHabits(habits: List<Habit>) {
        prefs.edit().putString(KEY_HABITS, gson.toJson(habits)).apply()
    }
    // endregion

    // region Moods
    fun getMoods(): MutableList<MoodEntry> {
        val json = prefs.getString(KEY_MOODS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<MoodEntry>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun saveMoods(entries: List<MoodEntry>) {
        prefs.edit().putString(KEY_MOODS, gson.toJson(entries)).apply()
    }
    // endregion

    // region Hydration settings
    fun getDailyWaterTargetMl(): Int = prefs.getInt(KEY_WATER_TARGET, 2000)
    fun setDailyWaterTargetMl(value: Int) {
        prefs.edit().putInt(KEY_WATER_TARGET, value).apply()
    }

    fun getReminderIntervalMinutes(): Int = prefs.getInt(KEY_WATER_INTERVAL_MIN, 60)
    fun setReminderIntervalMinutes(value: Int) {
        prefs.edit().putInt(KEY_WATER_INTERVAL_MIN, value).apply()
    }

    fun getReminderStartMinutes(): Int = prefs.getInt(KEY_WATER_START_MIN, 8 * 60)
    fun setReminderStartMinutes(value: Int) {
        prefs.edit().putInt(KEY_WATER_START_MIN, value).apply()
    }

    fun getReminderEndMinutes(): Int = prefs.getInt(KEY_WATER_END_MIN, 22 * 60)
    fun setReminderEndMinutes(value: Int) {
        prefs.edit().putInt(KEY_WATER_END_MIN, value).apply()
    }
    
    fun getCurrentWaterIntake(): Int = prefs.getInt(KEY_CURRENT_WATER, 0)
    fun setCurrentWaterIntake(value: Int) {
        prefs.edit().putInt(KEY_CURRENT_WATER, value).apply()
    }
    
    fun addWaterIntake(amount: Int) {
        val current = getCurrentWaterIntake()
        setCurrentWaterIntake(current + amount)
    }
    
    fun resetWaterIntake() {
        setCurrentWaterIntake(0)
    }
    // endregion

    companion object {
        private const val PREFS_NAME = "wellness_prefs"
        private const val KEY_HABITS = "habits_json"
        private const val KEY_MOODS = "moods_json"
        private const val KEY_WATER_TARGET = "water_target_ml"
        private const val KEY_WATER_INTERVAL_MIN = "water_interval_min"
        private const val KEY_WATER_START_MIN = "water_start_min"
        private const val KEY_WATER_END_MIN = "water_end_min"
        private const val KEY_CURRENT_WATER = "current_water"
    }
}


