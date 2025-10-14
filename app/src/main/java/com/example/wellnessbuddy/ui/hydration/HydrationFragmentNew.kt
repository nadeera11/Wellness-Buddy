package com.example.wellnessbuddy.ui.hydration

import android.animation.ValueAnimator
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wellnessbuddy.databinding.FragmentHydrationNewBinding
import com.example.wellnessbuddy.viewmodel.SettingsViewModel
import com.example.wellnessbuddy.workers.HydrationScheduler
import com.example.wellnessbuddy.ui.widgets.CircularProgressView
import java.util.*

class HydrationFragmentNew : Fragment() {
    private var _binding: FragmentHydrationNewBinding? = null
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
        _binding = FragmentHydrationNewBinding.inflate(inflater, container, false)
        
        initializeViews()
        setupObservers()
        setupClickListeners()
        setupSpinner()
        
        return binding.root
    }
    
    private fun initializeViews() {
        // Initialize with current values
        val target = vm.waterTarget.value ?: 2000
        val current = vm.currentWater.value ?: 0
        val startMin = vm.startMin.value ?: (8 * 60)
        val endMin = vm.endMin.value ?: (22 * 60)
        
        updateProgressDisplay(current, target)
        binding.etDailyTarget.setText(target.toString())
        updateTimeDisplay(startMin, true)
        updateTimeDisplay(endMin, false)
    }
    
    private fun setupObservers() {
        // Water progress observer
        vm.currentWater.observe(viewLifecycleOwner) { current ->
            val target = vm.waterTarget.value ?: 2000
            updateProgressDisplay(current, target)
        }

        vm.waterTarget.observe(viewLifecycleOwner) { target ->
            val current = vm.currentWater.value ?: 0
            updateProgressDisplay(current, target)
        }

        // Time settings observers
        vm.startMin.observe(viewLifecycleOwner) { minutes ->
            updateTimeDisplay(minutes, true)
        }

        vm.endMin.observe(viewLifecycleOwner) { minutes ->
            updateTimeDisplay(minutes, false)
        }
    }
    
    private fun setupClickListeners() {
        // Water intake buttons
        binding.btn50ml.setOnClickListener { addWater(50) }
        binding.btn100ml.setOnClickListener { addWater(100) }
        binding.btn200ml.setOnClickListener { addWater(200) }
        binding.btn250ml.setOnClickListener { addWater(250) }
        binding.btn500ml.setOnClickListener { addWater(500) }

        // Reset button
        binding.btnReset.setOnClickListener {
            vm.resetWaterIntake()
        }

        // Time pickers
        binding.btnStartTime.setOnClickListener { showTimePicker(true) }
        binding.btnEndTime.setOnClickListener { showTimePicker(false) }

        // Daily target text watcher
        binding.etDailyTarget.addTextChangedListener { text ->
            if (!isUpdatingTarget) {
                text?.toString()?.toIntOrNull()?.let { target ->
                    if (target >= 500 && target <= 5000) {
                        isUpdatingTarget = true
                        vm.setWaterTarget(target)
                        isUpdatingTarget = false
                    }
                }
            }
        }
        
        // Access profile icon from included header layout
        binding.root.findViewById<android.widget.ImageView>(com.example.wellnessbuddy.R.id.ivProfile)?.setOnClickListener {
            // Navigate to profile screen
            parentFragmentManager.beginTransaction()
                .replace(com.example.wellnessbuddy.R.id.fragment_container, com.example.wellnessbuddy.ui.profile.ProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }
    
    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            intervalOptions.map { it.first }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerInterval.adapter = adapter

        // Set current selection
        vm.intervalMin.observe(viewLifecycleOwner) { currentInterval ->
            val index = intervalOptions.indexOfFirst { it.second == currentInterval }
            if (index >= 0 && !isUpdatingSpinner) {
                isUpdatingSpinner = true
                binding.spinnerInterval.setSelection(index)
                isUpdatingSpinner = false
            }
        }

        // Handle selection changes
        binding.spinnerInterval.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (!isUpdatingSpinner) {
                        val selectedInterval = intervalOptions[position].second
                        vm.setInterval(selectedInterval)
                        // Reschedule with new interval
                        HydrationScheduler.schedule(requireContext())
                    }
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        )
    }
    
    private fun addWater(amount: Int) {
        vm.addWaterIntake(amount)
    }
    
    private fun updateProgressDisplay(current: Int, target: Int) {
        val percentage = if (target > 0) {
            ((current.toFloat() / target) * 100).toInt().coerceIn(0, 100)
        } else 0

        binding.tvPercentage.text = "${percentage}%"
        binding.tvProgress.text = "$current / $target ml"
        
        // Update circular progress view
        binding.circularProgressView.setProgress(percentage.toFloat(), animate = true)
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
