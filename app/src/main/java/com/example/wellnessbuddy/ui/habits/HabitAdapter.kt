package com.example.wellnessbuddy.ui.habits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnessbuddy.R
import com.example.wellnessbuddy.data.model.Habit
import com.example.wellnessbuddy.databinding.ItemHabitBinding
import java.text.SimpleDateFormat
import java.util.*

class HabitAdapter(
    private var items: List<Habit>,
    private val onMarkComplete: (Habit) -> Unit,
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
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val todayProgress = item.progressByYmd[today] ?: 0
        
        // Basic info
        holder.binding.tvIcon.text = item.icon
        holder.binding.tvName.text = item.name
        
        // Streak (placeholder - you can implement streak calculation later)
        holder.binding.tvStreak.text = "1 days"
        
        // Progress circles
        setupProgressCircles(holder.binding.layoutProgressCircles, item.dailyTarget, todayProgress)
        
        // Mark complete button visibility
        if (todayProgress < item.dailyTarget) {
            holder.binding.btnMarkComplete.visibility = View.VISIBLE
            holder.binding.btnMarkComplete.setOnClickListener { onMarkComplete(item) }
        } else {
            holder.binding.btnMarkComplete.visibility = View.GONE
        }
        
        // Action buttons
        holder.binding.btnEdit.setOnClickListener { onEdit(item) }
        holder.binding.btnDelete.setOnClickListener { onDelete(item) }
    }

    private fun setupProgressCircles(container: ViewGroup, target: Int, completed: Int) {
        container.removeAllViews()
        
        for (i in 0 until target) {
            val imageView = ImageView(container.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    container.context.resources.getDimensionPixelSize(R.dimen.progress_circle_size),
                    container.context.resources.getDimensionPixelSize(R.dimen.progress_circle_size)
                ).apply {
                    marginEnd = container.context.resources.getDimensionPixelSize(R.dimen.progress_circle_margin)
                }
                
                if (i < completed) {
                    setImageResource(R.drawable.ic_check_circle)
                    setBackgroundResource(R.drawable.bg_progress_completed)
                } else {
                    setImageResource(R.drawable.ic_check_circle_gray)
                    setBackgroundResource(R.drawable.bg_progress_incomplete)
                }
                
                setPadding(4, 4, 4, 4)
            }
            container.addView(imageView)
        }
    }

    fun submitList(newItems: List<Habit>) {
        items = newItems
        notifyDataSetChanged()
    }
}


