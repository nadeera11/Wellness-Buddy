package com.example.wellnessbuddy.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wellnessbuddy.databinding.ActivityAuthBinding
import com.example.wellnessbuddy.MainActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.btnRegister.setOnClickListener {
            performRegister()
        }
    }

    //Preferences using for login
    private fun performLogin() {
        val username = binding.etUsername.text?.toString()?.trim()
        val password = binding.etPassword.text?.toString()?.trim()

        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            showError("Please enter both username and password")
            return
        }

        // Check if user exists and password matches
        val savedUsername = prefs.getString("username", "")
        val savedPassword = prefs.getString("password", "")

        if (username == savedUsername && password == savedPassword) {
            // Login successful
            prefs.edit().putBoolean("is_logged_in", true).apply()
            navigateToMain()
        } else {
            showError("Invalid username or password")
        }
    }

    private fun performRegister() {
        val username = binding.etUsername.text?.toString()?.trim()
        val password = binding.etPassword.text?.toString()?.trim()

        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            showError("Please enter both username and password")
            return
        }

        if (password.length < 4) {
            showError("Password must be at least 4 characters")
            return
        }

        // Check if user already exists
        val existingUsername = prefs.getString("username", "")
        if (username == existingUsername) {
            showError("Username already exists")
            return
        }

        // Register new user
        prefs.edit()
            .putString("username", username)
            .putString("password", password)
            .putBoolean("is_logged_in", true)
            .apply()

        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
        navigateToMain()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = android.view.View.VISIBLE
        
        // Hide error after 3 seconds
        binding.tvErrorMessage.postDelayed({
            binding.tvErrorMessage.visibility = android.view.View.GONE
        }, 3000)
    }
}
