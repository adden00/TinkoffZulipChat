package com.addenisov00.courseproject.presentation.main_screen.channels.elm

import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.common.states.ChannelsType


data class ChannelsState(
    val myChannelsList: List<DelegateItem>? = null,
    val allChannelsList: List<DelegateItem>? = null,
    val searchedChannels: List<DelegateItem> = listOf(),
    val isSearching: Boolean = false,
    val currentChannelsType: ChannelsType = ChannelsType.MY_CHANNELS
) {
    fun currentList(): List<DelegateItem>? = when (currentChannelsType) {
        ChannelsType.MY_CHANNELS -> myChannelsList
        ChannelsType.ALL_CHANNELS -> allChannelsList
    }
}