package com.example.wellnessbuddy.ui.hydration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wellnessbuddy.databinding.FragmentHydrationBinding
import com.example.wellnessbuddy.viewmodel.SettingsViewModel

class HydrationFragment : Fragment() {
    private var _binding: FragmentHydrationBinding? = null
    private val binding get() = _binding!!
    private val vm: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHydrationBinding.inflate(inflater, container, false)
        vm.waterTarget.observe(viewLifecycleOwner) { binding.tvWaterTarget.text = "Target: ${it} ml" }
        vm.intervalMin.observe(viewLifecycleOwner) { binding.tvInterval.text = "Interval: ${it} min" }

        binding.btnStart.setOnClickListener {
            com.example.wellnessbuddy.workers.HydrationScheduler.schedule(requireContext())
        }
        binding.btnStop.setOnClickListener {
            com.example.wellnessbuddy.workers.HydrationScheduler.cancel(requireContext())
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


