package com.example.wellnessbuddy.ui.hydration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wellnessbuddy.databinding.FragmentHydrationSimpleBinding
import com.example.wellnessbuddy.viewmodel.SettingsViewModel

class HydrationFragmentSimple : Fragment() {
    private var _binding: FragmentHydrationSimpleBinding? = null
    private val binding get() = _binding!!
    private val vm: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHydrationSimpleBinding.inflate(inflater, container, false)
        setupSimpleObservers()
        setupSimpleClickListeners()
        return binding.root
    }

    private fun setupSimpleObservers() {
        // Water progress - simple display
        vm.currentWater.observe(viewLifecycleOwner) { current ->
            vm.waterTarget.value?.let { target ->
                val percentage = if (target > 0) {
                    ((current.toFloat() / target) * 100).toInt().coerceIn(0, 100)
                } else 0

                binding.tvPercentage.text = "${percentage}%"
                binding.tvProgress.text = "$current / $target ml"
            }
        }

        vm.waterTarget.observe(viewLifecycleOwner) { target ->
            vm.currentWater.value?.let { current ->
                val percentage = if (target > 0) {
                    ((current.toFloat() / target) * 100).toInt().coerceIn(0, 100)
                } else 0

                binding.tvPercentage.text = "${percentage}%"
                binding.tvProgress.text = "$current / $target ml"
            }
        }

        // Settings display
        vm.waterTarget.observe(viewLifecycleOwner) { target ->
            vm.intervalMin.value?.let { interval ->
                binding.tvSettings.text = "Target: ${target}ml, Interval: ${interval}min"
            }
        }

        vm.intervalMin.observe(viewLifecycleOwner) { interval ->
            vm.waterTarget.value?.let { target ->
                binding.tvSettings.text = "Target: ${target}ml, Interval: ${interval}min"
            }
        }
    }

    private fun setupSimpleClickListeners() {
        // Water intake buttons
        binding.btn50ml.setOnClickListener { vm.addWaterIntake(50) }
        binding.btn100ml.setOnClickListener { vm.addWaterIntake(100) }
        binding.btn200ml.setOnClickListener { vm.addWaterIntake(200) }
        binding.btn250ml.setOnClickListener { vm.addWaterIntake(250) }
        binding.btn500ml.setOnClickListener { vm.addWaterIntake(500) }

        // Reset button
        binding.btnReset.setOnClickListener {
            vm.resetWaterIntake()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
