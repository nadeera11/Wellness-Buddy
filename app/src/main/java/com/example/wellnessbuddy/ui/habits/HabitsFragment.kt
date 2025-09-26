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
import java.text.SimpleDateFormat
import java.util.*

class HabitsFragment : Fragment() {
    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!
    private val vm: HabitsViewModel by viewModels()
    private lateinit var adapter: HabitAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupClickListeners()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = HabitAdapter(
            items = emptyList(),
            onMarkComplete = { habit ->
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                vm.incrementProgressToday(habit.id, today)
            },
            onEdit = { habit ->
                HabitDialog.show(requireContext()) { result ->
                    val updated = habit.copy(name = result.name, icon = result.icon, dailyTarget = result.target)
                    vm.updateHabit(updated)
                }
            },
            onDelete = { habit ->
                vm.deleteHabit(habit.id)
            }
        )
        
        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabits.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.fabAdd.setOnClickListener {
            showAddHabitDialog()
        }
        
        binding.btnAddFirstHabit.setOnClickListener {
            showAddHabitDialog()
        }
    }

    private fun setupObservers() {
        vm.habits.observe(viewLifecycleOwner) { habits ->
            adapter.submitList(habits)
            
            // Show/hide empty state
            if (habits.isEmpty()) {
                binding.layoutEmptyState.visibility = View.VISIBLE
                binding.rvHabits.visibility = View.GONE
            } else {
                binding.layoutEmptyState.visibility = View.GONE
                binding.rvHabits.visibility = View.VISIBLE
            }
        }
    }

    private fun showAddHabitDialog() {
        HabitDialog.show(requireContext()) { result ->
            vm.addHabit(result.name, result.icon, result.target)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


