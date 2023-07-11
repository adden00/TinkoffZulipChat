package com.addenisov00.courseproject.presentation.main_screen.channels.elm

import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem

sealed class ChannelsEffect {
    object ShowErrorWhileChannelsLoading : ChannelsEffect()
    object ShowErrorWhileTopicsLoading : ChannelsEffect()
    object ShowLoading : ChannelsEffect()
    object HideLoading : ChannelsEffect()
    object ShowCreatingLoader : ChannelsEffect()
    object HideCreatingLoader : ChannelsEffect()
    object ShowErrorWithDatabase : ChannelsEffect()
    object ShowChannelAlreadyExist : ChannelsEffect()
    object ShowErrorWhileCreating : ChannelsEffect()
    object ShowChannelCreated : ChannelsEffect()
    object ShowTopicsLoader : ChannelsEffect()
    object HideTopicsLoader : ChannelsEffect()
    data class OpenChannel(
        val channelItem: ChannelItem
    ) : ChannelsEffect()
}