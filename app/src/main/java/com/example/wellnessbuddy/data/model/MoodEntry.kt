package com.example.wellnessbuddy.data.model

/**
 * A single mood log captured at a timestamp with an emoji and optional note.
 */
data class MoodEntry(
    val id: String,
    val timestamp: Long,
    val emoji: String,
    val note: String? = null
)


