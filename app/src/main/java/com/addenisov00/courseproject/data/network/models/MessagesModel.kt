package com.addenisov00.courseproject.data.network.models

import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.common.utils.HtmlHelper
import com.addenisov00.courseproject.presentation.custom_views.ReactionType
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import com.addenisov00.courseproject.presentation.messenger.models.ReactionItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMessagesResponse(
    @SerialName("messages")
    val messages: List<MessageDto>,
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String
)

@Serializable
data class GetOneMessageResponse(
    @SerialName("message")
    val receivedMessage: MessageDto,
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String
)

@Serializable
data class AddPhotoResponse(
    @SerialName("result")
    val result: String,
    @SerialName("msg")
    val message: String,
    @SerialName("uri")
    val uri: String
)

@Serializable
data class MessageDto(
    @SerialName("id")
    val id: Int,
    @SerialName("content")
    val content: String,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("sender_full_name")
    val senderName: String,
    @SerialName("avatar_url")
    val senderAvatarUrl: String,
    @SerialName("reactions")
    val reactions: List<ReactionDto>
)

@Serializable
data class SendMessageResult(
    @SerialName("id")
    val id: Int,
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String
)


@Serializable
data class ReactionDto(
    @SerialName("user_id")
    val userId: Int = -1,
    @SerialName("emoji_name")
    val name: String,
    @SerialName("emoji_code")
    val code: String,
    @SerialName("reaction_type")
    val type: String
)


fun ReactionDto.toReactionItem(count: Int, isSelected: Boolean): ReactionItem =
    ReactionItem(
        reaction = getReaction(code),
        count = count,
        isSelected = isSelected,
        reactionName = name,
        reactionType = ReactionType.REACTION
    )


fun MessageDto.toMessageItem(htmlHelper: HtmlHelper): MessageItem =
    MessageItem(
        id = id,
        userName = senderName,
        userId = senderId,
        content = htmlHelper.fromHtml(content),
        reactions = reactions.map { mappingReaction ->
            val count = reactions.count {
                it.code == mappingReaction.code
            }
            val isSelected: Boolean = reactions.find {
                it.userId == Constants.MY_USER_ID && it.code == mappingReaction.code
            } != null
            mappingReaction.toReactionItem(count, isSelected)
        }.distinct(),
        isMy = senderId == Constants.MY_USER_ID,
        imageUrl = senderAvatarUrl,
        timeStamp = timestamp
    )

fun getReaction(code: String): String {
    return try {
        String(Character.toChars(Integer.decode("0x$code")))
    } catch (e: NumberFormatException) {
        ""
    }
}
