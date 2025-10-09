package com.example.wellnessbuddy.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wellnessbuddy.databinding.FragmentProfileBinding
import com.example.wellnessbuddy.ui.onboarding.OnboardingActivity
import com.example.wellnessbuddy.viewmodel.HabitsViewModel
import com.example.wellnessbuddy.viewmodel.MoodsViewModel
import com.example.wellnessbuddy.viewmodel.SettingsViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences
    
    // ViewModels for getting stats
    private val habitsVm: HabitsViewModel by viewModels()
    private val moodsVm: MoodsViewModel by viewModels()
    private val settingsVm: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        
        prefs = requireContext().getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
        
        setupClickListeners()
        setupObservers()
        loadUserInfo()
        
        return binding.root
    }
    
    private fun setupClickListeners() {
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    
    private fun setupObservers() {
        // Update stats when data changes
        habitsVm.habits.observe(viewLifecycleOwner) { habits ->
            binding.tvHabitsCount.text = habits.size.toString()
        }
        
        moodsVm.entries.observe(viewLifecycleOwner) { entries ->
            binding.tvMoodsCount.text = entries.size.toString()
        }
        
        settingsVm.currentWater.observe(viewLifecycleOwner) { water ->
            binding.tvWaterCount.text = "${water}ml"
        }
    }
    
    private fun loadUserInfo() {
        // Load user info from SharedPreferences
        val username = prefs.getString("username", "Wellness User") ?: "Wellness User"
        binding.tvUserName.text = username
    }

    // Shared preferences for Logout process
    private fun logout() {
        // Clear login state
        prefs.edit().putBoolean("is_logged_in", false).apply()
        
        // Navigate to onboarding
        val intent = Intent(requireContext(), OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        
        // Finish current activity
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
