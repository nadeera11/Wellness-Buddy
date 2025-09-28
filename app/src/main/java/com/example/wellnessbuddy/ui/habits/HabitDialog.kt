package com.example.wellnessbuddy.ui.habits

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.wellnessbuddy.databinding.DialogHabitBinding

object HabitDialog {
    data class Result(val name: String, val icon: String, val target: Int)

    fun show(context: Context, onResult: (Result) -> Unit) {
        val binding = DialogHabitBinding.inflate(LayoutInflater.from(context))
        AlertDialog.Builder(context)
            .setTitle("Add Habit")
            .setView(binding.root)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Save") { _, _ ->
                val name = binding.etName.text.toString().trim()
                val icon = binding.etIcon.text.toString().ifBlank { "🏃" }
                val target = binding.etTarget.text.toString().toIntOrNull() ?: 1
                onResult(Result(name, icon, target))
            }
            .show()
    }
}


