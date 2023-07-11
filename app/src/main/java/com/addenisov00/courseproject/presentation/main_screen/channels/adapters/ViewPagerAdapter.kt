package com.addenisov00.courseproject.presentation.main_screen.channels.adapters

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.addenisov00.courseproject.common.states.ChannelsType
import com.addenisov00.courseproject.presentation.main_screen.channels.ChannelsListFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val channelsTypes: List<ChannelsType>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = channelsTypes.size
    override fun createFragment(position: Int) =
        ChannelsListFragment.newInstance(channelsTypes[position])
}