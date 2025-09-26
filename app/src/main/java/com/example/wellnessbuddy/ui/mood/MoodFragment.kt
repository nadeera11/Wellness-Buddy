package com.example.wellnessbuddy.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellnessbuddy.databinding.FragmentMoodBinding
import com.example.wellnessbuddy.ui.common.EmojiPickerDialog
import com.example.wellnessbuddy.viewmodel.MoodsViewModel

class MoodFragment : Fragment() {
    private var _binding: FragmentMoodBinding? = null
    private val binding get() = _binding!!
    private val vm: MoodsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoodBinding.inflate(inflater, container, false)
        val adapter = MoodAdapter(emptyList(), onDelete = { entry ->
            vm.deleteMood(entry.id)
        })
        binding.rvMoods.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMoods.adapter = adapter
        vm.entries.observe(viewLifecycleOwner) { adapter.submitList(it) }

        binding.btnAddMood.setOnClickListener {
            EmojiPickerDialog.show(requireContext()) { emoji ->
                vm.addMood(System.currentTimeMillis(), emoji, null)
            }
        }
        // Navigate to trend fragment via simple fragment transaction from overflow long-click on Add
        binding.btnAddMood.setOnLongClickListener {
            parentFragmentManager.beginTransaction()
                .replace(com.example.wellnessbuddy.R.id.fragment_container, MoodTrendFragment())
                .addToBackStack(null)
                .commit()
            true
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


