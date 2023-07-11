package com.addenisov00.courseproject.domain

import com.addenisov00.courseproject.common.states.ChannelCreateResult
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem
import kotlinx.coroutines.flow.Flow

interface ChannelsRepository {

    suspend fun updateMyChannelsFromNetwork()
    suspend fun updateAllChannelsFromNetwork()

    fun loadMyChannelsFromCache(): Flow<List<ChannelItem>>
    fun getAllChannelsFromCache(): Flow<List<ChannelItem>>

    suspend fun getTopicsFromNetwork(channel: ChannelItem): List<TopicItem>
    suspend fun getTopicsFromCache(channelId: Int): List<TopicItem>

    suspend fun createChannel(channelName: String, channelDescription: String): ChannelCreateResult


}