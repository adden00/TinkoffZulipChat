package com.addenisov00.courseproject.presentation.messenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.addenisov00.courseproject.common.Constants.MESSAGE_BOTTOM_DIALOG_ACTION_KEY
import com.addenisov00.courseproject.common.Constants.MESSAGE_CALLBACK_KEY
import com.addenisov00.courseproject.common.OnClickListener
import com.addenisov00.courseproject.common.customGetSerializable
import com.addenisov00.courseproject.common.states.BottomDialogActions
import com.addenisov00.courseproject.common.states.DialogType
import com.addenisov00.courseproject.databinding.BottomSheetDialogReactionBinding
import com.addenisov00.courseproject.presentation.messenger.adapters.EmojisAdapter
import com.addenisov00.courseproject.presentation.messenger.models.EmojiListItem
import com.addenisov00.courseproject.presentation.utills.EmojiGetter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmojiBottomSheet(
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDialogReactionBinding? = null
    private val binding get() = _binding!!
    private val emojisAdapter: EmojisAdapter by lazy {
        EmojisAdapter(EmojiClickListener())
    }

    private val clickListener by lazy {
        arguments?.customGetSerializable(MESSAGE_CALLBACK_KEY) as? (BottomDialogActions) -> Unit
    }
    private val dialogType by lazy {
        arguments?.customGetSerializable(MESSAGE_BOTTOM_DIALOG_ACTION_KEY) as? DialogType
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDialogReactionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setEmojiList()
        setMenuButtons()
        dialogType?.let {
            when (it) {
                DialogType.OPEN_MENU -> showMenu()
                DialogType.ADD_REACTION -> showEmojis()
            }
        }
    }

    private fun showMenu() {
        binding.rcEmojis.visibility = View.GONE
        binding.bottomSheetMenu.visibility = View.VISIBLE
    }

    private fun showEmojis() {
        binding.rcEmojis.visibility = View.VISIBLE
        binding.bottomSheetMenu.visibility = View.GONE
    }

    private fun setMenuButtons() = with(binding) {
        btnCopyMessage.setOnClickListener {
            clickListener?.let {
                it(BottomDialogActions.CopyMessage)
            }
            findNavController().navigateUp()
        }

        btnDeleteMessage.setOnClickListener {
            clickListener?.let {
                it(BottomDialogActions.DeleteMessage)
            }
            findNavController().navigateUp()
        }

        btnEditMessage.setOnClickListener {
            clickListener?.let {
                it(BottomDialogActions.EditMessage)
            }
            findNavController().navigateUp()
        }

        btnAddReaction.setOnClickListener {
            rcEmojis.visibility = View.VISIBLE
            bottomSheetMenu.visibility = View.GONE
        }
    }

    private fun setEmojiList() {
        binding.rcEmojis.adapter = emojisAdapter
        binding.rcEmojis.layoutManager = GridLayoutManager(binding.root.context, 5)
        val emojis = EmojiGetter.emojiSet
        emojisAdapter.submitList(emojis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private inner class EmojiClickListener : OnClickListener<EmojiListItem> {
        override fun onClick(item: EmojiListItem) {
            clickListener?.let {
                it(BottomDialogActions.AddReaction(item))
            }
            findNavController().navigateUp()
        }
    }
}