package com.example.wellnessbuddy.ui.hydration

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wellnessbuddy.R
import com.example.wellnessbuddy.databinding.FragmentHydrationBinding
import com.example.wellnessbuddy.viewmodel.SettingsViewModel
import com.example.wellnessbuddy.workers.HydrationScheduler

class HydrationFragment : Fragment() {
    private var _binding: FragmentHydrationBinding? = null
    private val binding get() = _binding!!
    private val vm: SettingsViewModel by viewModels()
    private var isUpdatingTarget = false
    private var isUpdatingSpinner = false

    private val waterAmounts = listOf(50, 100, 200, 250, 500)
    private val intervalOptions = listOf(
        "Every 30 minutes" to 30,
        "Every 1 hour" to 60,
        "Every 1.5 hours" to 90,
        "Every 2 hours" to 120,
        "Every 3 hours" to 180
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHydrationBinding.inflate(inflater, container, false)
        
        // Initialize with current values immediately
        initializeViews()
        
        // Set up basic observers without complex logic
        setupBasicObservers()
        setupBasicClickListeners()
        
        return binding.root
    }
    
    private fun initializeViews() {
        try {
            val target = vm.waterTarget.value ?: 2000
            val current = vm.currentWater.value ?: 0
            val interval = vm.intervalMin.value ?: 60
            
            updateProgressDisplay(current, target)
            binding.etDailyTarget.setText(target.toString())
            
            // Initialize time displays
            val startMin = vm.startMin.value ?: (8 * 60)
            val endMin = vm.endMin.value ?: (22 * 60)
            updateTimeDisplay(startMin, true)
            updateTimeDisplay(endMin, false)
        } catch (e: Exception) {
            // Fallback to defaults if there's any issue
            binding.tvPercentage.text = "0%"
            binding.tvProgress.text = "0 / 2000 ml"
            binding.etDailyTarget.setText("2000")
        }
    }

    private fun setupBasicObservers() {
        // Simple water progress observer
        vm.currentWater.observe(viewLifecycleOwner) { current ->
            val target = vm.waterTarget.value ?: 2000
            updateProgressDisplay(current, target)
        }

        vm.waterTarget.observe(viewLifecycleOwner) { target ->
            val current = vm.currentWater.value ?: 0
            updateProgressDisplay(current, target)
        }
    }
    
    private fun updateProgressDisplay(current: Int, target: Int) {
        val percentage = if (target > 0) {
            ((current.toFloat() / target) * 100).toInt().coerceIn(0, 100)
        } else 0

        binding.tvPercentage.text = "${percentage}%"
        binding.tvProgress.text = "$current / $target ml"
    }
    
    private fun updateTimeDisplay(minutes: Int, isStartTime: Boolean) {
        val hours = minutes / 60
        val mins = minutes % 60
        val timeText = String.format("%02d:%02d", hours, mins)
        
        if (isStartTime) {
            binding.btnStartTime.text = timeText
        } else {
            binding.btnEndTime.text = timeText
        }
    }

    private fun setupBasicClickListeners() {
        // Water intake buttons - simple and direct
        binding.btn50ml.setOnClickListener { vm.addWaterIntake(50) }
        binding.btn100ml.setOnClickListener { vm.addWaterIntake(100) }
        binding.btn200ml.setOnClickListener { vm.addWaterIntake(200) }
        binding.btn250ml.setOnClickListener { vm.addWaterIntake(250) }
        binding.btn500ml.setOnClickListener { vm.addWaterIntake(500) }

        // Reset button
        binding.btnReset.setOnClickListener {
            vm.resetWaterIntake()
        }

        // Time pickers - simplified
        binding.btnStartTime.setOnClickListener { 
            try {
                showTimePicker(true)
            } catch (e: Exception) {
                // Handle any time picker issues
            }
        }
        binding.btnEndTime.setOnClickListener { 
            try {
                showTimePicker(false) 
            } catch (e: Exception) {
                // Handle any time picker issues
            }
        }
    }

    // Removed complex spinner setup to prevent freezing

    private fun showTimePicker(isStartTime: Boolean) {
        val currentMinutes = if (isStartTime) {
            vm.startMin.value ?: (8 * 60)
        } else {
            vm.endMin.value ?: (22 * 60)
        }

        val hours = currentMinutes / 60
        val minutes = currentMinutes % 60

        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val totalMinutes = hourOfDay * 60 + minute
                if (isStartTime) {
                    vm.setStart(totalMinutes)
                } else {
                    vm.setEnd(totalMinutes)
                }
                // Reschedule with new times
                HydrationScheduler.schedule(requireContext())
            },
            hours,
            minutes,
            true
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


