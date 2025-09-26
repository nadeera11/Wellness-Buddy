package com.example.wellnessbuddy

import android.app.Application
import com.example.wellnessbuddy.util.NotifUtils

class WellnessBuddyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotifUtils.ensureChannels(this)
    }
}


