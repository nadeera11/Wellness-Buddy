package com.example.wellnessbuddy.ui.mood

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellnessbuddy.databinding.FragmentMoodTsxBinding
import com.example.wellnessbuddy.databinding.DialogMoodFormBinding
import com.example.wellnessbuddy.databinding.ItemEmojiPickerBinding
import com.example.wellnessbuddy.databinding.ItemMoodHistoryBinding
import com.example.wellnessbuddy.viewmodel.MoodsViewModel
import com.example.wellnessbuddy.data.model.MoodEntry
import com.example.wellnessbuddy.R
import java.text.SimpleDateFormat
import java.util.*

class MoodFragmentTSX : Fragment() {
    private var _binding: FragmentMoodTsxBinding? = null
    private val binding get() = _binding!!
    private val vm: MoodsViewModel by viewModels()
    
    private var currentViewMode = "list" // "list" or "calendar"
    private lateinit var moodHistoryAdapter: MoodHistoryAdapter
    private lateinit var emojiPickerAdapter: EmojiPickerAdapter
    
    private val emojis = listOf("😊", "😢", "😴", "😡", "😍", "😰", "🤔", "😎", "🥳", "😌", "😤", "😭", "😠", "🤢", "😮", "😑", "😋", "🤗")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoodTsxBinding.inflate(inflater, container, false)
        
        setupRecyclerViews()
        setupClickListeners()
        setupObservers()
        
        return binding.root
    }
    
    private fun setupRecyclerViews() {
        // Mood History Adapter
        moodHistoryAdapter = MoodHistoryAdapter(
            items = emptyList(),
            onDelete = { moodEntry ->
                vm.deleteMood(moodEntry.id)
            }
        )
        
        binding.rvMoodHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMoodHistory.adapter = moodHistoryAdapter
        
        // Calendar Adapter (placeholder for now)
        // TODO: Implement calendar adapter
    }
    
    private fun setupClickListeners() {
        // View Toggle Buttons
        binding.btnListView.setOnClickListener {
            switchToView("list")
        }
        
        binding.btnCalendarView.setOnClickListener {
            switchToView("calendar")
        }
        
        // Add Mood FAB
        binding.fabAddMood.setOnClickListener {
            showMoodFormDialog()
        }
    }
    
    private fun setupObservers() {
        vm.entries.observe(viewLifecycleOwner) { entries ->
            moodHistoryAdapter.submitList(entries)
            
            // Show/hide empty state
            if (entries.isEmpty()) {
                binding.layoutEmptyState.visibility = View.VISIBLE
                binding.rvMoodHistory.visibility = View.GONE
            } else {
                binding.layoutEmptyState.visibility = View.GONE
                binding.rvMoodHistory.visibility = View.VISIBLE
            }
        }
    }
    
    private fun switchToView(viewMode: String) {
        currentViewMode = viewMode
        
        if (viewMode == "list") {
            // Switch to list view
            binding.layoutListView.visibility = View.VISIBLE
            binding.layoutCalendarView.visibility = View.GONE
            
            // Update button states
            binding.btnListView.setBackgroundResource(R.drawable.view_toggle_button_selected)
            binding.btnCalendarView.setBackgroundResource(R.drawable.view_toggle_button_unselected)
            
        } else {
            // Switch to calendar view
            binding.layoutListView.visibility = View.GONE
            binding.layoutCalendarView.visibility = View.VISIBLE
            
            // Update button states
            binding.btnListView.setBackgroundResource(R.drawable.view_toggle_button_unselected)
            binding.btnCalendarView.setBackgroundResource(R.drawable.view_toggle_button_selected)
        }
    }
    
    private fun showMoodFormDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogMoodFormBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        
        var selectedEmoji = emojis[0] // Default to first emoji
        
        // Setup emoji picker
        emojiPickerAdapter = EmojiPickerAdapter(
            emojis = emojis,
            selectedEmoji = selectedEmoji,
            onEmojiSelected = { emoji ->
                selectedEmoji = emoji
                emojiPickerAdapter.updateSelection(emoji)
            }
        )
        
        dialogBinding.rvEmojis.layoutManager = GridLayoutManager(requireContext(), 6)
        dialogBinding.rvEmojis.adapter = emojiPickerAdapter
        
        // Dialog actions
        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialogBinding.btnSave.setOnClickListener {
            val note = dialogBinding.etNote.text?.toString()?.trim()
            if (note.isNullOrEmpty()) {
                vm.addMood(System.currentTimeMillis(), selectedEmoji, null)
            } else {
                vm.addMood(System.currentTimeMillis(), selectedEmoji, note)
            }
            
            Toast.makeText(requireContext(), "Mood entry added!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Mood History Adapter
class MoodHistoryAdapter(
    private var items: List<MoodEntry>,
    private val onDelete: (MoodEntry) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<MoodHistoryAdapter.ViewHolder>() {
    
    class ViewHolder(private val binding: ItemMoodHistoryBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(moodEntry: MoodEntry, onDelete: (MoodEntry) -> Unit) {
            binding.tvEmoji.text = moodEntry.emoji
            
            val date = Date(moodEntry.timestamp)
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            val dateText = dateFormat.format(date)
            val timeText = timeFormat.format(date)
            binding.tvDate.text = "$dateText at $timeText"
            
            if (!moodEntry.note.isNullOrEmpty()) {
                binding.tvNote.text = moodEntry.note
                binding.tvNote.visibility = View.VISIBLE
            } else {
                binding.tvNote.visibility = View.GONE
            }
            
            binding.btnDelete.setOnClickListener {
                onDelete(moodEntry)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMoodHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onDelete)
    }
    
    override fun getItemCount(): Int = items.size
    
    fun submitList(newItems: List<MoodEntry>) {
        items = newItems
        notifyDataSetChanged()
    }
}

// Emoji Picker Adapter
class EmojiPickerAdapter(
    private val emojis: List<String>,
    private var selectedEmoji: String,
    private val onEmojiSelected: (String) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<EmojiPickerAdapter.ViewHolder>() {
    
    class ViewHolder(private val binding: ItemEmojiPickerBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(emoji: String, isSelected: Boolean, onEmojiSelected: (String) -> Unit) {
            binding.tvEmoji.text = emoji
            
            // Update selection state
            if (isSelected) {
                binding.tvEmoji.setBackgroundResource(R.drawable.emoji_background_selected)
            } else {
                binding.tvEmoji.setBackgroundResource(R.drawable.emoji_background)
            }
            
            binding.root.setOnClickListener {
                onEmojiSelected(emoji)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmojiPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emoji = emojis[position]
        val isSelected = emoji == selectedEmoji
        holder.bind(emoji, isSelected, onEmojiSelected)
    }
    
    override fun getItemCount(): Int = emojis.size
    
    fun updateSelection(newSelectedEmoji: String) {
        val oldIndex = emojis.indexOf(selectedEmoji)
        val newIndex = emojis.indexOf(newSelectedEmoji)
        
        selectedEmoji = newSelectedEmoji
        
        if (oldIndex >= 0) notifyItemChanged(oldIndex)
        if (newIndex >= 0) notifyItemChanged(newIndex)
    }
}
