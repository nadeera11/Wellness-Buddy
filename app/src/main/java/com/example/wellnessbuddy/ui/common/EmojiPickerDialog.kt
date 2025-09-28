package com.example.wellnessbuddy.ui.common

import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter

object EmojiPickerDialog {
    private val EMOJIS = listOf("😀","🙂","😐","😕","😢","😭","😡","😴","😫","🤒","🤢","🥳","😎","😍","🧘")

    fun show(context: Context, onPick: (String) -> Unit) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, EMOJIS)
        AlertDialog.Builder(context)
            .setTitle("Pick an emoji")
            .setAdapter(adapter) { _, which -> onPick(EMOJIS[which]) }
            .setNegativeButton("Cancel", null)
            .show()
    }
}


