package com.example.wellnessbuddy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.wellnessbuddy.ui.home.HomeFragment
import com.example.wellnessbuddy.ui.habits.HabitsFragment
import com.example.wellnessbuddy.ui.mood.MoodFragmentTSX
import com.example.wellnessbuddy.ui.hydration.HydrationFragmentNew
import com.example.wellnessbuddy.ui.onboarding.OnboardingActivity

class MainActivity : AppCompatActivity() {
    // Getting SharedPreferences instance
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if user is logged in
        prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
        
        if (!isLoggedIn) {
            // User not logged in, go to onboarding
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val bottom = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottom.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_habits -> HabitsFragment()
                R.id.nav_mood -> MoodFragmentTSX()
                R.id.nav_hydration -> HydrationFragmentNew()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            true
        }
        if (savedInstanceState == null) {
            bottom.selectedItemId = R.id.nav_home
        }
    }
}