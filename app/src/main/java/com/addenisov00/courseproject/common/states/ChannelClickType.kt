package com.addenisov00.courseproject.common.states

import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem

sealed class ChannelClickType(val channelItem: ChannelItem) {
    class ChannelItemClicked(channelItem: ChannelItem) : ChannelClickType(channelItem)
    class ExpandArrowClicked(channelItem: ChannelItem) : ChannelClickType(channelItem)
}