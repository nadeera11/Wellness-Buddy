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

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val habitsVm: HabitsViewModel by viewModels()
    private val moodsVm: MoodsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        habitsVm.habits.observe(viewLifecycleOwner) { list ->
            val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            val totalTargets = list.sumOf { it.dailyTarget.coerceAtLeast(0) }
            val totalDone = list.sumOf { it.progressByYmd[today] ?: 0 }
            val pct = if (totalTargets == 0) 0 else (totalDone * 100 / totalTargets).coerceIn(0, 100)
            binding.tvSummary.text = "Habits: ${pct}% today"
        }
        moodsVm.entries.observe(viewLifecycleOwner) { list ->
            val last = list.firstOrNull()
            if (last != null) {
                val time = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(last.timestamp))
                binding.tvSummary.append("\nLast mood: ${last.emoji} at ${time}")
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


