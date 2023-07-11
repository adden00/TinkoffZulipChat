package com.addenisov00.courseproject.presentation.main_screen.channels.elm

import com.addenisov00.courseproject.common.states.ChannelsType
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem

sealed class ChannelsCommand {
    object SubscribeOnMyChannels : ChannelsCommand()
    object SubscribeOnAllChannels : ChannelsCommand()
    object UpdateMyChannels : ChannelsCommand()
    object UpdateAllChannels : ChannelsCommand()

    data class LoadTopics(
        val channelItem: ChannelItem,
        val channelsType: ChannelsType
    ) : ChannelsCommand()

    data class UpdateTopics(
        val channelItem: ChannelItem,
        val channelsType: ChannelsType
    ) : ChannelsCommand()

    data class CreateChannel(
        val channelName: String,
        val channelDescription: String
    ) : ChannelsCommand()
}