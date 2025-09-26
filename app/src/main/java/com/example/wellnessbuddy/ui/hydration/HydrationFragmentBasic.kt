package com.example.wellnessbuddy.ui.hydration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wellnessbuddy.databinding.FragmentHydrationBasicBinding
import com.example.wellnessbuddy.viewmodel.SettingsViewModel

class HydrationFragmentBasic : Fragment() {
    private var _binding: FragmentHydrationBasicBinding? = null
    private val binding get() = _binding!!
    private val vm: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHydrationBasicBinding.inflate(inflater, container, false)
        
        // Simple setup without complex observers
        updateDisplay()
        
        binding.btn50ml.setOnClickListener { 
            vm.addWaterIntake(50)
            updateDisplay()
        }
        
        binding.btn100ml.setOnClickListener { 
            vm.addWaterIntake(100)
            updateDisplay()
        }
        
        binding.btnReset.setOnClickListener {
            vm.resetWaterIntake()
            updateDisplay()
        }
        
        return binding.root
    }
    
    private fun updateDisplay() {
        val current = vm.currentWater.value ?: 0
        val target = vm.waterTarget.value ?: 2000
        val percentage = if (target > 0) {
            ((current.toFloat() / target) * 100).toInt().coerceIn(0, 100)
        } else 0

        binding.tvPercentage.text = "${percentage}%"
        binding.tvProgress.text = "$current / $target ml"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
