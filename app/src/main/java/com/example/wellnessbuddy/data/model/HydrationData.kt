package com.example.wellnessbuddy.data.model

data class HydrationData(
    val current: Int = 0,
    val target: Int = 2000,
    val reminderInterval: Int = 60, // minutes
    val startTime: String = "08:00",
    val endTime: String = "22:00"
)
