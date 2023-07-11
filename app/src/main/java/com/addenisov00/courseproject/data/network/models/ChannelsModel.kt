package com.addenisov00.courseproject.data.network.models

import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllChannelsResponse(
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String,
    @SerialName("streams")
    val channels: List<ChannelDto>
)

@Serializable
data class MyChannelsResponse(
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String,
    @SerialName("subscriptions")
    val channels: List<ChannelDto>
)

@Serializable
data class CreateChannelResponse(
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String,
    @SerialName("subscribed")
    val subscribed: Map<String, List<String>>,
    @SerialName("already_subscribed")
    val alreadySubscribed: Map<String, List<String>>
)


@Serializable
data class ChannelDto(
    @SerialName("date_created")
    val dateCreated: Int,
    @SerialName("description")
    val description: String,
    @SerialName("name")
    val name: String,
    @SerialName("stream_id")
    val id: Int,
    @SerialName("color")
    val color: String? = null,
    @SerialName("is_muted")
    val isMuted: Boolean? = null,
    @SerialName("pin_to_top")
    val pinToTop: Boolean? = null
)

fun ChannelDto.toChannelItem(): ChannelItem {
    return ChannelItem(
        name = this.name,
        id = this.id,
        isExpand = false
    )
}