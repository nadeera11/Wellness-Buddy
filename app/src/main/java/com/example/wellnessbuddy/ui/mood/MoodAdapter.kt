package com.example.wellnessbuddy.ui.mood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnessbuddy.data.model.MoodEntry
import com.example.wellnessbuddy.databinding.ItemMoodBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MoodAdapter(
    private var items: List<MoodEntry>,
    private val onDelete: (MoodEntry) -> Unit = {}
) : RecyclerView.Adapter<MoodAdapter.Holder>() {
    inner class Holder(val binding: ItemMoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemMoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.binding.tvEmoji.text = item.emoji
        holder.binding.tvNote.text = item.note ?: ""
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.binding.tvTime.text = sdf.format(Date(item.timestamp))
        holder.binding.btnDelete.setOnClickListener { onDelete(item) }
    }

    fun submitList(newItems: List<MoodEntry>) {
        items = newItems
        notifyDataSetChanged()
    }
}


