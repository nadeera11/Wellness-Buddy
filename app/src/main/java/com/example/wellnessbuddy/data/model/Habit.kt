package com.example.wellnessbuddy.data.model

/**
 * Represents a daily habit the user wants to track.
 *
 * [id] is a stable identifier used for updates/deletes.
 * [name] is a user-visible title.
 * [icon] is an emoji string (e.g., "🏃") kept simple to avoid drawable plumbing.
 * [dailyTarget] is how many times per day the habit should be completed.
 * [progressByYmd] maps YYYY-MM-DD → number of completions that day.
 */
data class Habit(
    val id: String,
    var name: String,
    var icon: String,
    var dailyTarget: Int,
    val progressByYmd: MutableMap<String, Int> = mutableMapOf()
)


