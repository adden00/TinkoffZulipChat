package com.addenisov00.courseproject.data.repository

import com.addenisov00.courseproject.common.states.ChannelCreateResult
import com.addenisov00.courseproject.data.local.dao.ChannelsDao
import com.addenisov00.courseproject.data.mappers.toAllChannelEntity
import com.addenisov00.courseproject.data.mappers.toChannelItem
import com.addenisov00.courseproject.data.mappers.toEntity
import com.addenisov00.courseproject.data.mappers.toMyChannelEntity
import com.addenisov00.courseproject.data.mappers.toTopicItem
import com.addenisov00.courseproject.data.network.MessageApiClient
import com.addenisov00.courseproject.data.network.models.toChannelItem
import com.addenisov00.courseproject.data.network.models.toTopicItem
import com.addenisov00.courseproject.domain.ChannelsRepository
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChannelsRepositoryImpl(private val api: MessageApiClient, private val dao: ChannelsDao) :
    ChannelsRepository {


    override fun loadMyChannelsFromCache(): Flow<List<ChannelItem>> =
        dao.getMyChannels()
            .map { channels ->
                channels
                    .map { it.toChannelItem() }
                    .sortedBy { it.name }
            }


    override fun getAllChannelsFromCache(): Flow<List<ChannelItem>> =
        dao.getAllChannels()
            .map { channels ->
                channels
                    .map { it.toChannelItem() }
                    .sortedBy { it.name }
            }

    override suspend fun updateMyChannelsFromNetwork() {
        val result = api.getMyChannels().channels.map { it.toChannelItem() }
        dao.deleteMyChannels()
        dao.insertMyChannels(result.map { it.toMyChannelEntity() })
    }

    override suspend fun updateAllChannelsFromNetwork() {
        val result = api.getAllChannels().channels.map { it.toChannelItem() }
        dao.deleteAllChannels()
        dao.insertAllChannels(result.map { it.toAllChannelEntity() })
    }


    override suspend fun getTopicsFromNetwork(channel: ChannelItem): List<TopicItem> {
        val result = api.getTopics(channel.id).topics.map { it.toTopicItem(channel) }
        dao.insertTopicsToChannel(result.map { it.toEntity() })
        return result
    }

    override suspend fun getTopicsFromCache(channelId: Int): List<TopicItem> {
        val result = dao.getTopicsFromChannel(channelId)
        return result.map { it.toTopicItem() }
    }

    override suspend fun createChannel(
        channelName: String,
        channelDescription: String
    ): ChannelCreateResult {
        val channelInfo = "[{\"name\": \"$channelName\", \"description\": \"$channelDescription\"}]"
        val result = api.createChannel(channelInfo)
        return if (result.subscribed.isNotEmpty())
            ChannelCreateResult.CREATED
        else
            ChannelCreateResult.ALREADY_EXIST
    }
}