package com.example.wellnessbuddy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wellnessbuddy.databinding.FragmentHomeBinding
import androidx.fragment.app.viewModels
import com.example.wellnessbuddy.viewmodel.HabitsViewModel
import com.example.wellnessbuddy.viewmodel.MoodsViewModel
import com.example.wellnessbuddy.viewmodel.SettingsViewModel
import com.example.wellnessbuddy.ui.common.EmojiPickerDialog
import com.example.wellnessbuddy.ui.mood.MoodTrendFragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val habitsVm: HabitsViewModel by viewModels()
    private val moodsVm: MoodsViewModel by viewModels()
    private val settingsVm: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupObservers()
        setupClickListeners()
        updateDate()
        setupChart()
        return binding.root
    }

    private fun setupObservers() {
        // Habits progress
        habitsVm.habits.observe(viewLifecycleOwner) { list ->
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val completedHabits = list.count { habit ->
                val progress = habit.progressByYmd[today] ?: 0
                progress >= habit.dailyTarget
            }
            val totalHabits = list.size
            val pct = if (totalHabits == 0) 0 else (completedHabits * 100 / totalHabits).coerceIn(0, 100)
            
            binding.tvHabitsPercent.text = "${pct}%"
            updateProgressBar(binding.progressHabitsView, pct)
        }

        // Current mood
        moodsVm.entries.observe(viewLifecycleOwner) { list ->
            val latest = list.firstOrNull()
            if (latest != null) {
                binding.tvCurrentMoodEmoji.text = latest.emoji
                binding.tvCurrentMoodNote.text = latest.note ?: "No notes"
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(latest.timestamp))
                binding.tvCurrentMoodTime.text = time
                binding.layoutMoodContent.visibility = View.VISIBLE
                binding.tvNoMoodMessage.visibility = View.GONE
            } else {
                binding.layoutMoodContent.visibility = View.GONE
                binding.tvNoMoodMessage.visibility = View.VISIBLE
            }
            
            // Update chart
            updateMoodChart(list)
        }

        // Hydration (placeholder - you can implement water tracking later)
        settingsVm.waterTarget.observe(viewLifecycleOwner) { target ->
            // For now, just show 0% - you can implement actual water intake tracking
            binding.tvHydrationPercent.text = "0%"
            updateProgressBar(binding.progressHydrationView, 0)
        }
    }

    private fun setupClickListeners() {
        binding.btnAddMoodEntry.setOnClickListener {
            EmojiPickerDialog.show(requireContext()) { emoji ->
                moodsVm.addMood(System.currentTimeMillis(), emoji, null)
            }
        }

        binding.btnViewAllTrends.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(com.example.wellnessbuddy.R.id.fragment_container, MoodTrendFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateDate() {
        val dateFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(Date())
    }

    private fun updateProgressBar(progressView: View, percentage: Int) {
        val params = progressView.layoutParams
        val parentWidth = (progressView.parent as View).width
        if (parentWidth > 0) {
            params.width = (parentWidth * percentage / 100)
            progressView.layoutParams = params
        } else {
            // Delay until parent is measured
            progressView.post {
                val parentWidthPost = (progressView.parent as View).width
                params.width = (parentWidthPost * percentage / 100)
                progressView.layoutParams = params
            }
        }
    }

    private fun setupChart() {
        binding.moodTrendChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            setTouchEnabled(false)
            setScaleEnabled(false)
            setPinchZoom(false)
        }
    }

    private fun updateMoodChart(entries: List<com.example.wellnessbuddy.data.model.MoodEntry>) {
        // Simple chart showing mood entries count per day over last 7 days
        val cal = Calendar.getInstance()
        val today = cal.timeInMillis
        val chartEntries = mutableListOf<Entry>()
        
        for (i in 6 downTo 0) {
            cal.timeInMillis = today - (i * 24 * 60 * 60 * 1000L)
            val dayStart = cal.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            val dayEnd = dayStart + 24 * 60 * 60 * 1000L
            val dayEntries = entries.count { it.timestamp in dayStart until dayEnd }
            chartEntries.add(Entry((6 - i).toFloat(), dayEntries.toFloat()))
        }
        
        val dataSet = LineDataSet(chartEntries, "Mood Entries").apply {
            color = android.graphics.Color.parseColor("#3B82F6")
            setCircleColor(android.graphics.Color.parseColor("#3B82F6"))
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false)
        }
        
        binding.moodTrendChart.data = LineData(dataSet)
        binding.moodTrendChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


