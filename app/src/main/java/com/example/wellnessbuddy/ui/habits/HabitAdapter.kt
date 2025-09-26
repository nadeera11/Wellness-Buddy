package com.example.wellnessbuddy.ui.habits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnessbuddy.data.model.Habit
import com.example.wellnessbuddy.databinding.ItemHabitBinding

class HabitAdapter(
    private var items: List<Habit>,
    private val onPlus: (Habit) -> Unit,
    private val computeProgressText: (Habit) -> String,
    private val onEdit: (Habit) -> Unit,
    private val onDelete: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.Holder>() {

    inner class Holder(val binding: ItemHabitBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.binding.tvIcon.text = item.icon
        holder.binding.tvName.text = item.name
        holder.binding.tvProgress.text = computeProgressText(item)
        holder.binding.btnPlus.setOnClickListener { onPlus(item) }
        holder.binding.btnEdit.setOnClickListener { onEdit(item) }
        holder.binding.btnDelete.setOnClickListener { onDelete(item) }
    }

    fun submitList(newItems: List<Habit>) {
        items = newItems
        notifyDataSetChanged()
    }
}


