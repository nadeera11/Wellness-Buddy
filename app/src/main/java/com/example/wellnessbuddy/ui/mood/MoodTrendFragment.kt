package com.example.wellnessbuddy.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wellnessbuddy.databinding.FragmentMoodTrendBinding
import com.example.wellnessbuddy.viewmodel.MoodsViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.Calendar

class MoodTrendFragment : Fragment() {
    private var _binding: FragmentMoodTrendBinding? = null
    private val binding get() = _binding!!
    private val vm: MoodsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoodTrendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.entries.observe(viewLifecycleOwner) { entries ->
            // Map last 7 days to counts per day (very simple metric)
            val cal = Calendar.getInstance()
            val dayToCount = IntArray(7) { 0 }
            val todayStart = cal.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            entries.forEach { e ->
                val daysAgo = ((todayStart - (e.timestamp - todayStart)) / (24L * 60 * 60 * 1000)).toInt()
                val idx = (6 - daysAgo).coerceIn(0, 6)
                dayToCount[idx]++
            }
            val points = dayToCount.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) }
            val dataSet = LineDataSet(points, "Mood entries per day")
            binding.chart.data = LineData(dataSet)
            binding.chart.invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


