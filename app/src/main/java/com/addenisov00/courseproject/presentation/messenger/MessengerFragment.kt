package com.addenisov00.courseproject.presentation.messenger

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.common.customGetParcelable
import com.addenisov00.courseproject.common.delegate_utills.BaseAdapter
import com.addenisov00.courseproject.common.di.ui.DaggerMessageComponent
import com.addenisov00.courseproject.common.states.BottomDialogActions
import com.addenisov00.courseproject.common.states.DialogType
import com.addenisov00.courseproject.common.states.InfoForMessenger
import com.addenisov00.courseproject.common.states.MessageInputType
import com.addenisov00.courseproject.databinding.FragmentMessengerBinding
import com.addenisov00.courseproject.getAppComponent
import com.addenisov00.courseproject.presentation.custom_views.ReactionType
import com.addenisov00.courseproject.presentation.messenger.adapters.DateDelegatedAdapter
import com.addenisov00.courseproject.presentation.messenger.adapters.MessageDelegatedAdapter
import com.addenisov00.courseproject.presentation.messenger.elm.MessageEffect
import com.addenisov00.courseproject.presentation.messenger.elm.MessageState
import com.addenisov00.courseproject.presentation.messenger.elm.MessagesEvent
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import com.addenisov00.courseproject.presentation.messenger.models.ReactionItem
import com.addenisov00.courseproject.presentation.utills.MessageClickListener
import com.google.android.material.snackbar.Snackbar
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class MessengerFragment : ElmFragment<MessagesEvent, MessageEffect, MessageState>() {
    private var _binding: FragmentMessengerBinding? = null
    private val binding get() = _binding!!
    private val adapter: BaseAdapter by lazy { BaseAdapter() }

    @Inject
    lateinit var messageViewModelFactory: MessengerViewModelFactory

    private val viewModel: MessageViewModel by viewModels { messageViewModelFactory }
    private val currentChannelAndTopic: InfoForMessenger by lazy {
        arguments?.customGetParcelable<InfoForMessenger>(Constants.TOPIC_TO_MESSENGER_KEY) as InfoForMessenger
    }
    override val initEvent: MessagesEvent
        get() = MessagesEvent.Ui.Init(currentChannelAndTopic)

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("MyLog", "uri: $uri")

                store.accept(
                    MessagesEvent.Ui.SendPicture(
                        picture = uri,
                        currentItem = currentChannelAndTopic,
                        contentResolver = requireContext().contentResolver
                    )
                )
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val adapterSubmitListener = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            store.accept(MessagesEvent.Ui.ItemsInserted)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerMessageComponent.factory().create(requireContext().getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessengerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBar()
        setUI()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.unregisterAdapterDataObserver(adapterSubmitListener)
    }


    private fun setStatusBar() {
        requireActivity().window.statusBarColor = requireContext().getColor(R.color.color_primary)
    }


    private fun setUI() {
        binding.messengerToolbar.title = currentChannelAndTopic.getName()
        binding.rcMessages.adapter = adapter.apply {
            registerAdapterDataObserver(adapterSubmitListener)
            addDelegate(DateDelegatedAdapter())
            addDelegate(
                MessageDelegatedAdapter(
                    OnMessageClickListener()
                )
            )
        }
        binding.rcMessages.itemAnimator?.addDuration = 0
        binding.rcMessages.itemAnimator?.changeDuration = 0
        binding.rcMessages.itemAnimator?.moveDuration = 0
        binding.fabScrollDown.hide()
        binding.btnSendMessage.setOnClickListener {
            store.accept(
                MessagesEvent.Ui.ButtonSendClicked(
                    binding.edTypeText.text?.toString() ?: "", currentChannelAndTopic
                )
            )
        }

        binding.edTypeText.addTextChangedListener { text ->
            store.accept(MessagesEvent.Ui.InputTextChanged(text?.toString() ?: ""))
        }

        binding.messengerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.rcMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rcMessages.layoutManager as LinearLayoutManager
                val position = layoutManager.findFirstVisibleItemPosition()
                val holdPaginationPosition = layoutManager.findLastVisibleItemPosition()
                if (dy > 0) {
                    binding.fabScrollDown.show()
                } else
                    binding.fabScrollDown.hide()
                if (position < Constants.LOAD_NEXT_PAGE_MESSAGE_COUNT && dy < 0)
                    store.accept(
                        MessagesEvent.Ui.LoadMoreMessages(
                            currentItem = currentChannelAndTopic,
                            anchor = adapter.currentList.filterIsInstance<MessageItem>()
                                .getOrNull(0)?.id ?: "newest",
                            holdPaginationPosition = adapter.currentList.filterIsInstance<MessageItem>()
                                .getOrNull(holdPaginationPosition)?.id ?: 0
                        )
                    )
            }
        })
        binding.btnRetry.setOnClickListener {
            store.accept(MessagesEvent.Ui.Init(currentChannelAndTopic))
        }

        binding.fabScrollDown.setOnClickListener {
            scrollDown()
            binding.fabScrollDown.hide()
        }
    }


    override fun createStore(): Store<MessagesEvent, MessageEffect, MessageState> {
        return viewModel.messagesStore()
    }

    override fun render(state: MessageState) {
        adapter.submitList(state.currentList)
    }

    override fun handleEffect(effect: MessageEffect) {
        when (effect) {
            is MessageEffect.ShowError -> showSnackBar(getString(R.string.error))
            is MessageEffect.ShowLoader -> showLoader()
            is MessageEffect.HideLoader -> hideLoader()
            is MessageEffect.ShowMessageLoader -> showMessageLoader()
            is MessageEffect.HideMessageLoader -> hideMessageLoader()
            is MessageEffect.ScrollDown -> scrollDown()
            is MessageEffect.ScrollToMessage -> scrollToMessage(effect.messageId)
            is MessageEffect.ShowSuccessDeleted -> showSnackBar(getString(R.string.deleted))
            is MessageEffect.ShowSuccessEdited -> showSnackBar(getString(R.string.message_edited))
            is MessageEffect.ShowErrorWhileDeleted -> showSnackBar(getString(R.string.error_while_deleting))
            is MessageEffect.ShowErrorWhileEdited -> showSnackBar(getString(R.string.edit_message_unknown_error))
            is MessageEffect.ShowLateErrorWhileEdited -> showSnackBar(getString(R.string.edit_message_late_error))
            is MessageEffect.ShowDatabaseError -> showDatabaseError()
            is MessageEffect.HideDatabaseError -> hideDatabaseError()
            is MessageEffect.OpenPhotoPicker -> openPhotoPicker()
            is MessageEffect.ClearInputText -> clearInputText()
            is MessageEffect.SetSendButtonLogo -> setSendButtonLogo(effect.inputType)
            is MessageEffect.PutMessageToEditText -> putMessageToEditText(effect.message)
        }
    }


    private fun hideLoader() {
        binding.pbLoading.visibility = View.GONE
    }

    private fun showLoader() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun showSnackBar(
        messageText: String,
        actionText: String = "",
        action: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(binding.root, messageText, Snackbar.LENGTH_SHORT)
        action?.let {
            snackbar.setAction(actionText) { action() }
        }
        snackbar.show()
    }


    private fun showToast(toast: String) {
        Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
    }

    private fun scrollDown() {
        binding.rcMessages.scrollToPosition(adapter.itemCount - 1)
    }

    private fun showMessageLoader() {
        binding.pbMessagesLoading.visibility = View.VISIBLE
    }

    private fun hideMessageLoader() {
        binding.pbMessagesLoading.visibility = View.GONE
    }

    private fun scrollToMessage(messageId: Int) {
        val position = adapter.currentList.indexOfFirst { it.id() == messageId }
        if (position > 1)
            binding.rcMessages.scrollToPosition(position - 2)
    }

    private fun showDatabaseError() {
        binding.ErrorMessage.visibility = View.VISIBLE
    }

    private fun hideDatabaseError() {
        binding.ErrorMessage.visibility = View.GONE
    }

    private fun openPhotoPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    private fun clearInputText() {
        binding.edTypeText.setText("")
    }

    private fun setSendButtonLogo(inputType: MessageInputType) {
        when (inputType) {
            MessageInputType.FILE -> {
                binding.btnSendMessage.background = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.button_send_plus
                )
            }

            MessageInputType.TEXT -> {
                binding.btnSendMessage.background = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.button_send_plane
                )
            }
        }
    }

    private fun putMessageToEditText(message: String) {
        binding.edTypeText.requestFocus()
        binding.edTypeText.setText(message)
    }


    private inner class OnMessageClickListener :
        MessageClickListener {
        override fun onMessageClick(message: MessageItem) {
            openAddDialog(message, DialogType.OPEN_MENU)
        }

        override fun onReactionClick(message: MessageItem, reaction: ReactionItem) {
            when (reaction.reactionType) {
                ReactionType.REACTION -> reactionClick(message, reaction)
                ReactionType.ADD_NEW_REACTION -> openAddDialog(message, DialogType.ADD_REACTION)
            }
        }
    }

    fun reactionClick(message: MessageItem, reaction: ReactionItem) {
        if (message.reactions.find { it == reaction }?.isSelected == true)
            store.accept(
                MessagesEvent.Ui.DeleteReaction(
                    message.id,
                    reaction.reactionName,
                    currentChannelAndTopic
                )
            )
        else
            store.accept(
                MessagesEvent.Ui.AddReaction(
                    message.id,
                    reaction.reactionName,
                    currentChannelAndTopic
                )
            )
    }

    fun openAddDialog(message: MessageItem, dialogType: DialogType) {
        val callback = { action: BottomDialogActions ->
            when (action) {
                is BottomDialogActions.AddReaction -> {
                    store.accept(
                        MessagesEvent.Ui.AddReaction(
                            message.id,
                            action.item.name,
                            currentChannelAndTopic
                        )
                    )
                }

                is BottomDialogActions.CopyMessage -> {
                    val copyResult = copyMessage(message.content.text)
                    if (copyResult)
                        showToast(getString(R.string.copy))
                    else
                        showToast(getString(R.string.error))
                }

                is BottomDialogActions.DeleteMessage -> store.accept(
                    MessagesEvent.Ui.DeleteMessage(
                        message.id
                    )
                )

                is BottomDialogActions.EditMessage -> {
                    if (message.isMy)
                        startEditing(message)
                    else {
                        showSnackBar(getString(R.string.this_message_is_not_your))
                    }
                }
            }
        }


        val bundle = bundleOf(
            Constants.MESSAGE_CALLBACK_KEY to callback,
            Constants.MESSAGE_BOTTOM_DIALOG_ACTION_KEY to dialogType
        )
        findNavController().navigate(R.id.action_messengerFragment_to_emojiBottomSheet, bundle)
    }


    private fun startEditing(message: MessageItem) {
        store.accept(MessagesEvent.Ui.StartEditing(message))
    }


    private fun copyMessage(content: String): Boolean {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText("Message", content)
        clipboard?.setPrimaryClip(clip)
        return clipboard != null
    }
}