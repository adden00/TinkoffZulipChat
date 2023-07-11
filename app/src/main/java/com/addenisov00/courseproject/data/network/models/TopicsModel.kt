package com.addenisov00.courseproject.data.network.models

import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponseModel(
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String,
    @SerialName("topics")
    val topics: List<Topic>
)

@Serializable
data class Topic(
    @SerialName("name")
    val name: String,
    @SerialName("max_id")
    val lastMessageId: Int
)


fun Topic.toTopicItem(channelItem: ChannelItem): TopicItem {
    return TopicItem(
        name = this.name,
        channelId = channelItem.id,
        channelName = channelItem.name
    )
}