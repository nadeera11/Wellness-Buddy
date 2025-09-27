package com.example.wellnessbuddy.data.repo

import com.example.wellnessbuddy.data.PrefsStore
import com.example.wellnessbuddy.data.model.MoodEntry
import java.util.UUID

class MoodsRepository(private val store: PrefsStore) {

    fun getAll(): List<MoodEntry> = store.getMoods().sortedByDescending { it.timestamp }

    fun addMood(timestamp: Long, emoji: String, note: String?): MoodEntry {
        val moods = store.getMoods()
        val entry = MoodEntry(
            id = UUID.randomUUID().toString(),
            timestamp = timestamp,
            emoji = emoji,
            note = note
        )
        moods.add(entry)
        store.saveMoods(moods)
        return entry
    }

    fun deleteMood(id: String) {
        val moods = store.getMoods().filterNot { it.id == id }
        store.saveMoods(moods)
    }
}


