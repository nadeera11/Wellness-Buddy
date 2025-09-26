package com.example.wellnessbuddy.ui.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellnessbuddy.databinding.FragmentHabitsBinding
import com.example.wellnessbuddy.viewmodel.HabitsViewModel

class HabitsFragment : Fragment() {
    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!
    private val vm: HabitsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        val adapter = HabitAdapter(emptyList(), onPlus = { habit ->
            // naive today key; proper util can be added later
            val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            vm.incrementProgressToday(habit.id, today)
        }, { habit ->
            val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            val done = habit.progressByYmd[today] ?: 0
            "$done/${habit.dailyTarget} today"
        }, onEdit = { habit ->
            HabitDialog.show(requireContext()) { result ->
                val updated = habit.copy(name = result.name, icon = result.icon, dailyTarget = result.target)
                vm.updateHabit(updated)
            }
        }, onDelete = { habit ->
            vm.deleteHabit(habit.id)
        })
        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabits.adapter = adapter

        vm.habits.observe(viewLifecycleOwner) { adapter.submitList(it) }

        binding.btnAdd.setOnClickListener {
            HabitDialog.show(requireContext()) { result ->
                vm.addHabit(result.name, result.icon, result.target)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


