package com.addenisov00.courseproject.presentation.main_screen.channels

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.addenisov00.courseproject.common.states.ChannelsType
import com.addenisov00.courseproject.databinding.FragmentChannelsBinding
import com.addenisov00.courseproject.presentation.main_screen.channels.adapters.ViewPagerAdapter
import com.addenisov00.courseproject.presentation.utills.CreateNewChannelHelper
import com.addenisov00.courseproject.presentation.utills.SearchHelper
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ChannelsFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private val pages: List<String> by lazy {
        ChannelsType.values().map { requireContext().getString(it.value) }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setUI() {
        setTabLayout()
        setSearchEditText()
        setFloatingButton()
    }

    private fun setTabLayout() {
        val channelTypes = ChannelsType.values().toList()
        binding.viewPagerChannels.adapter =
            ViewPagerAdapter(
                childFragmentManager,
                lifecycle,
                channelTypes
            )
        TabLayoutMediator(binding.tabLayout, binding.viewPagerChannels) { tab, pos ->
            tab.text = pages[pos]
        }.attach()

        registerTabCallback()
    }

    private fun setSearchEditText() = with(binding.editTextTextChannelName) {
        onFocusChangeListener =
            OnFocusChangeListener { _, isFocused ->
                if (isFocused)
                    binding.fbtnCreateNewChannel.hide()
                else
                    binding.fbtnCreateNewChannel.show()
            }

        addTextChangedListener {
            lifecycleScope.launch {
                it?.toString()?.let { currentText ->
                    (findCurrentChannelFragment() as? SearchHelper)?.search(currentText)
                }
            }
        }
    }

    private fun setFloatingButton() {
        binding.fbtnCreateNewChannel.setOnClickListener {
            (findCurrentChannelFragment() as? CreateNewChannelHelper)?.createNewChannel()
        }
    }


    private fun registerTabCallback() {
        binding.viewPagerChannels.registerOnPageChangeCallback(changePageCallback)
    }


    fun hideKeyboard(activity: Activity?) {
        activity?.currentFocus?.let { view ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun findCurrentChannelFragment(): Fragment? {
        val fragmentTag = "f" + binding.viewPagerChannels.currentItem.toString()
        return childFragmentManager.findFragmentByTag(fragmentTag)
    }


    private val changePageCallback = object :
        ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            hideKeyboard(activity)
            binding.editTextTextChannelName.clearFocus()
            binding.editTextTextChannelName.setText("")
        }
    }
}

