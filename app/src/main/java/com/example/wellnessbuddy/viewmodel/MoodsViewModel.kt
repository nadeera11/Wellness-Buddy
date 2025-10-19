package com.example.wellnessbuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wellnessbuddy.data.PrefsStore
import com.example.wellnessbuddy.data.model.MoodEntry
import com.example.wellnessbuddy.data.repo.MoodsRepository
import com.example.wellnessbuddy.widget.WellnessWidgetProvider

class MoodsViewModel(application: Application) : AndroidViewModel(application) {
    private val store = PrefsStore(application)
    private val repo = MoodsRepository(store)

    private val _entries = MutableLiveData<List<MoodEntry>>(repo.getAll())
    val entries: LiveData<List<MoodEntry>> = _entries

    fun refresh() {
        _entries.value = repo.getAll()
    }

    fun addMood(timestamp: Long, emoji: String, note: String?) {
        repo.addMood(timestamp, emoji, note)
        refresh()
        // Update widget when mood data changes
        WellnessWidgetProvider.refreshAllWidgets(getApplication())
    }

    fun deleteMood(id: String) {
        repo.deleteMood(id)
        refresh()
        // Update widget when mood data changes
        WellnessWidgetProvider.refreshAllWidgets(getApplication())
    }
}


