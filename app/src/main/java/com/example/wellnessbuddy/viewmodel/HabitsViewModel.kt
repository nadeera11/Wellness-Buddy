package com.example.wellnessbuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wellnessbuddy.data.PrefsStore
import com.example.wellnessbuddy.data.model.Habit
import com.example.wellnessbuddy.data.repo.HabitsRepository
import com.example.wellnessbuddy.widget.WellnessWidgetProvider

class HabitsViewModel(application: Application) : AndroidViewModel(application) {
    private val store = PrefsStore(application)
    private val repo = HabitsRepository(store)

    private val _habits = MutableLiveData<List<Habit>>(repo.getAll())
    val habits: LiveData<List<Habit>> = _habits

    fun refresh() {
        _habits.value = repo.getAll()
    }

    fun addHabit(name: String, icon: String, dailyTarget: Int) {
        repo.addHabit(name, icon, dailyTarget)
        refresh()
        // Update widget when habits change
        WellnessWidgetProvider.refreshAllWidgets(getApplication())
    }

    fun updateHabit(habit: Habit) {
        repo.updateHabit(habit)
        refresh()
        // Update widget when habits change
        WellnessWidgetProvider.refreshAllWidgets(getApplication())
    }

    fun deleteHabit(id: String) {
        repo.deleteHabit(id)
        refresh()
        // Update widget when habits change
        WellnessWidgetProvider.refreshAllWidgets(getApplication())
    }

    fun incrementProgressToday(habitId: String, ymd: String) {
        repo.incrementProgress(habitId, ymd)
        refresh()
        // Update widget when habit progress changes
        WellnessWidgetProvider.refreshAllWidgets(getApplication())
    }

    fun getProgressPercentForDate(habit: Habit, ymd: String): Int {
        val done = habit.progressByYmd[ymd] ?: 0
        if (habit.dailyTarget <= 0) return 0
        val pct = (done * 100) / habit.dailyTarget
        return pct.coerceIn(0, 100)
    }
}


