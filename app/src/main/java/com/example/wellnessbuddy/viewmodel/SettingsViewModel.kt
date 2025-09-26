package com.example.wellnessbuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wellnessbuddy.data.PrefsStore

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val store = PrefsStore(application)

    private val _waterTarget = MutableLiveData(store.getDailyWaterTargetMl())
    val waterTarget: LiveData<Int> = _waterTarget

    private val _intervalMin = MutableLiveData(store.getReminderIntervalMinutes())
    val intervalMin: LiveData<Int> = _intervalMin

    private val _startMin = MutableLiveData(store.getReminderStartMinutes())
    val startMin: LiveData<Int> = _startMin

    private val _endMin = MutableLiveData(store.getReminderEndMinutes())
    val endMin: LiveData<Int> = _endMin

    fun setWaterTarget(ml: Int) {
        store.setDailyWaterTargetMl(ml)
        _waterTarget.value = ml
    }

    fun setInterval(minutes: Int) {
        store.setReminderIntervalMinutes(minutes)
        _intervalMin.value = minutes
    }

    fun setStart(minutes: Int) {
        store.setReminderStartMinutes(minutes)
        _startMin.value = minutes
    }

    fun setEnd(minutes: Int) {
        store.setReminderEndMinutes(minutes)
        _endMin.value = minutes
    }
}


