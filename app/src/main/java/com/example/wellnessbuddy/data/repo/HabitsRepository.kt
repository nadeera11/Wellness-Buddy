package com.example.wellnessbuddy.data.repo

import com.example.wellnessbuddy.data.PrefsStore
import com.example.wellnessbuddy.data.model.Habit
import java.util.UUID

class HabitsRepository(private val store: PrefsStore) {

    fun getAll(): List<Habit> = store.getHabits()

    fun addHabit(name: String, icon: String, dailyTarget: Int): Habit {
        val habits = store.getHabits()
        val habit = Habit(
            id = UUID.randomUUID().toString(),
            name = name,
            icon = icon,
            dailyTarget = dailyTarget
        )
        habits.add(habit)
        store.saveHabits(habits)
        return habit
    }

    fun updateHabit(updated: Habit) {
        val habits = store.getHabits()
        val index = habits.indexOfFirst { it.id == updated.id }
        if (index >= 0) {
            habits[index] = updated
            store.saveHabits(habits)
        }
    }

    fun deleteHabit(habitId: String) {
        val habits = store.getHabits().filterNot { it.id == habitId }
        store.saveHabits(habits)
    }

    fun incrementProgress(habitId: String, ymd: String): Int {
        val habits = store.getHabits()
        val habit = habits.firstOrNull { it.id == habitId } ?: return 0
        val current = habit.progressByYmd[ymd] ?: 0
        habit.progressByYmd[ymd] = current + 1
        store.saveHabits(habits)
        return habit.progressByYmd[ymd] ?: 0
    }

    fun setProgress(habitId: String, ymd: String, value: Int) {
        val habits = store.getHabits()
        val habit = habits.firstOrNull { it.id == habitId } ?: return
        habit.progressByYmd[ymd] = value
        store.saveHabits(habits)
    }

    fun resetProgressForDate(ymd: String) {
        val habits = store.getHabits()
        habits.forEach { it.progressByYmd.remove(ymd) }
        store.saveHabits(habits)
    }
}


