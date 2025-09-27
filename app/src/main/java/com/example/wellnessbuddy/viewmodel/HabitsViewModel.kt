package com.example.wellnessbuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wellnessbuddy.data.PrefsStore
import com.example.wellnessbuddy.data.model.Habit
import com.example.wellnessbuddy.data.repo.HabitsRepository

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
    }

    fun updateHabit(habit: Habit) {
        repo.updateHabit(habit)
        refresh()
    }

    fun deleteHabit(id: String) {
        repo.deleteHabit(id)
        refresh()
    }

    fun incrementProgressToday(habitId: String, ymd: String) {
        repo.incrementProgress(habitId, ymd)
        refresh()
    }

    fun getProgressPercentForDate(habit: Habit, ymd: String): Int {
        val done = habit.progressByYmd[ymd] ?: 0
        if (habit.dailyTarget <= 0) return 0
        val pct = (done * 100) / habit.dailyTarget
        return pct.coerceIn(0, 100)
    }
}


