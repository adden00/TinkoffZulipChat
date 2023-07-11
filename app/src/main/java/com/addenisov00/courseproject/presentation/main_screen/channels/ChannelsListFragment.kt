package com.addenisov00.courseproject.presentation.main_screen.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.common.OnClickListener
import com.addenisov00.courseproject.common.customGetSerializable
import com.addenisov00.courseproject.common.delegate_utills.BaseAdapter
import com.addenisov00.courseproject.common.di.ui.DaggerChannelsComponent
import com.addenisov00.courseproject.common.states.ChannelClickType
import com.addenisov00.courseproject.common.states.ChannelsType
import com.addenisov00.courseproject.common.states.InfoForMessenger
import com.addenisov00.courseproject.databinding.CreateChannelDialogBinding
import com.addenisov00.courseproject.databinding.FragmentChannelsListBinding
import com.addenisov00.courseproject.getAppComponent
import com.addenisov00.courseproject.presentation.main_screen.channels.adapters.ChannelsAdapter
import com.addenisov00.courseproject.presentation.main_screen.channels.adapters.ChannelsShimmerAdapter
import com.addenisov00.courseproject.presentation.main_screen.channels.adapters.TopicsAdapter
import com.addenisov00.courseproject.presentation.main_screen.channels.elm.ChannelsEffect
import com.addenisov00.courseproject.presentation.main_screen.channels.elm.ChannelsEvent
import com.addenisov00.courseproject.presentation.main_screen.channels.elm.ChannelsState
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem
import com.addenisov00.courseproject.presentation.utills.CreateNewChannelHelper
import com.addenisov00.courseproject.presentation.utills.SearchHelper
import com.google.android.material.snackbar.Snackbar
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChannelsListFragment : ElmFragment<ChannelsEvent, ChannelsEffect, ChannelsState>(),
    SearchHelper, CreateNewChannelHelper {

    private val adapter: BaseAdapter by lazy { BaseAdapter() }
    private var _binding: FragmentChannelsListBinding? = null
    private val binding get() = _binding!!

    private val channelsType by lazy {
        arguments?.customGetSerializable(CHANNELS_TYPE_KEY) as? ChannelsType
            ?: ChannelsType.MY_CHANNELS
    }


    @Inject
    lateinit var viewModelFactory: ChannelsStoreViewModelFactory
    private val viewModel: ChannelsViewModel by viewModels { viewModelFactory }

    override val initEvent: ChannelsEvent
        get() = ChannelsEvent.Ui.Init(channelsType)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerChannelsComponent.factory().create(requireContext().getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setUpUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUI() {
        binding.swipeToRefresh.setOnRefreshListener {
            store.accept(ChannelsEvent.Ui.UpdateChannels)
        }
    }

    private fun setAdapter() {
        binding.rcMyChannels.adapter = adapter.apply {
            this.addDelegate(ChannelsAdapter(ChannelsListener()))
            this.addDelegate(TopicsAdapter(TopicsListener()))
            this.addDelegate(ChannelsShimmerAdapter())
        }
    }

    override fun search(query: String) {
        store.accept(ChannelsEvent.Ui.SearchChannels(query))
    }

    override fun createStore(): Store<ChannelsEvent, ChannelsEffect, ChannelsState> {
        return viewModel.channelStore()
    }

    override fun render(state: ChannelsState) {
        if (state.isSearching) {
            adapter.submitList(state.searchedChannels)
        } else {

            if (state.currentList() == null) {
                store.accept(
                    ChannelsEvent.Ui.SubscribeOnChannels
                )
                store.accept(ChannelsEvent.Ui.UpdateChannels)
            } else
                adapter.submitList(state.currentList())
        }
    }

    override fun handleEffect(effect: ChannelsEffect) {
        when (effect) {
            is ChannelsEffect.ShowErrorWhileChannelsLoading -> showSnackBar(getString(R.string.error_while_channels_loading))
            is ChannelsEffect.ShowLoading -> showLoader()
            is ChannelsEffect.HideLoading -> hideLoader()
            is ChannelsEffect.ShowErrorWhileTopicsLoading -> showSnackBar(getString(R.string.error_while_loading_topics))
            is ChannelsEffect.ShowCreatingLoader -> showCreatingLoader()
            is ChannelsEffect.HideCreatingLoader -> hideCreatingLoader()
            is ChannelsEffect.ShowChannelAlreadyExist -> showSnackBar(getString(R.string.channel_already_exist))
            is ChannelsEffect.ShowErrorWhileCreating ->
                showSnackBar(
                    getString(R.string.error_while_creating),
                    actionText = getString(R.string.retry)
                ) {
                    (this as CreateNewChannelHelper).createNewChannel()
                }

            is ChannelsEffect.ShowErrorWithDatabase -> showErrorWithDatabase()
            is ChannelsEffect.ShowChannelCreated -> showSnackBar(getString(R.string.channel_created))
            is ChannelsEffect.ShowTopicsLoader -> showTopicsLoader()
            is ChannelsEffect.HideTopicsLoader -> hideTopicsLoader()
            is ChannelsEffect.OpenChannel -> navigateToMessenger(InfoForMessenger.Channel(effect.channelItem))
        }
    }


    private fun showLoader() {
        if (store.currentState.currentList()?.isEmpty() == true) {
            binding.rcMyChannels.visibility = View.GONE
            binding.channelsShimmerList.shimmer.visibility = View.VISIBLE
        }
    }

    private fun hideLoader() {
        binding.rcMyChannels.visibility = View.VISIBLE
        binding.channelsShimmerList.shimmer.visibility = View.GONE
        binding.swipeToRefresh.isRefreshing = false
    }


    private fun showCreatingLoader() {
        binding.pbarChannelCreating.visibility = View.VISIBLE
    }

    private fun hideCreatingLoader() {
        binding.pbarChannelCreating.visibility = View.GONE
        binding.swipeToRefresh.isRefreshing = false
    }

    private fun showErrorWithDatabase() {
        binding.rcMyChannels.visibility = View.GONE
        binding.channelsShimmerList.shimmer.visibility = View.GONE
        binding.tvEroorWithDb.visibility = View.VISIBLE
    }

    private fun showTopicsLoader() {
        if (store.currentState.currentList()?.filterIsInstance<TopicItem>()?.isEmpty() == true)
            binding.pbarTopicsLoading.visibility = View.VISIBLE
    }

    private fun hideTopicsLoader() {
        binding.pbarTopicsLoading.visibility = View.GONE
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

    private inner class ChannelsListener : OnClickListener<ChannelClickType> {
        override fun onClick(item: ChannelClickType) {
            store.accept(ChannelsEvent.Ui.OnChannelClick(item))
        }
    }

    private inner class TopicsListener : OnClickListener<TopicItem> {
        override fun onClick(item: TopicItem) {
            navigateToMessenger(InfoForMessenger.Topic(item))
        }
    }

    private fun navigateToMessenger(infoForMessage: InfoForMessenger) {

        val bundle = bundleOf(Constants.TOPIC_TO_MESSENGER_KEY to infoForMessage)
        (requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController.navigate(
            R.id.action_mainFragment_to_messengerFragment, bundle
        )
    }


    companion object {
        private const val CHANNELS_TYPE_KEY = "channels_type_key"
        fun newInstance(type: ChannelsType): Fragment {
            val args = Bundle()
            args.putSerializable(CHANNELS_TYPE_KEY, type)
            val fragment = ChannelsListFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun createNewChannel() {
        val dialogViewBinding =
            CreateChannelDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogViewBinding.root)
        val dialog = builder.create()
        dialogViewBinding.btnCreate.setOnClickListener {
            store.accept(
                ChannelsEvent.Ui.CreateNewChannel(
                    channelName = dialogViewBinding.edName.text.toString(),
                    channelDescription = dialogViewBinding.edDescription.text.toString(),
                )
            )
            dialog.dismiss()
        }
        dialogViewBinding.edName.addTextChangedListener {
            dialogViewBinding.btnCreate.isEnabled = it?.toString()?.isNotEmpty() ?: false
        }

        dialog.window?.decorView?.setBackgroundResource(R.drawable.dialog_bg) // setting the background
        dialog.show()
    }
}