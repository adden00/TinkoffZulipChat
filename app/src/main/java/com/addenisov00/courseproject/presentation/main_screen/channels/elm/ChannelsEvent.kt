package com.addenisov00.courseproject.presentation.main_screen.channels.elm

import com.addenisov00.courseproject.common.states.ChannelClickType
import com.addenisov00.courseproject.common.states.ChannelCreateResult
import com.addenisov00.courseproject.common.states.ChannelsType
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem

sealed class ChannelsEvent {
    sealed class Ui : ChannelsEvent() {

        object SubscribeOnChannels : Ui()
        object UpdateChannels : Ui()

        data class Init(val channelsType: ChannelsType) : Ui()

        data class OnChannelClick(
            val channelClickItem: ChannelClickType
        ) : ChannelsEvent()

        data class SearchChannels(
            val query: String
        ) : Ui()

        data class CreateNewChannel(
            val channelName: String,
            val channelDescription: String
        ) : Ui()

    }

    sealed class Internal : ChannelsEvent() {
        data class MyChannelsLoadedFromCache(val myChannelsList: List<ChannelItem>) : Internal()
        data class AllChannelsLoadedFromCache(val allChannelsList: List<ChannelItem>) : Internal()
        object MyChannelsUpdated : Internal()
        object AllChannelsUpdated : Internal()
        data class ErrorWhileChannelsLoading(val e: Throwable) : Internal()
        data class ErrorWhileTopicsLoading(val e: Throwable) : Internal()
        data class ErrorWhileCreatingChannel(val e: Throwable) : Internal()
        data class ErrorWithDatabase(val e: Throwable) : Internal()

        data class TopicsLoadedFromCache(
            val topicsList: List<TopicItem>,
            val channelItem: ChannelItem,
            val channelsType: ChannelsType
        ) : Internal()

        data class TopicsUpdated(
            val topicsList: List<TopicItem>,
            val channelItem: ChannelItem,
            val channelsType: ChannelsType
        ) : Internal()

        data class ChannelCreated(
            val channelCreateResult: ChannelCreateResult
        ) : Internal()

    }
}