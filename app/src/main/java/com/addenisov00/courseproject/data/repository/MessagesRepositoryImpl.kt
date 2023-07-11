package com.addenisov00.courseproject.data.repository

import android.content.ContentResolver
import android.net.Uri
import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.common.states.InfoForMessenger
import com.addenisov00.courseproject.common.utils.HtmlHelper
import com.addenisov00.courseproject.common.utils.HtmlHelperImpl
import com.addenisov00.courseproject.common.utils.InputStreamRequestBody
import com.addenisov00.courseproject.common.utils.NarrowUtils
import com.addenisov00.courseproject.data.local.dao.MessagesDao
import com.addenisov00.courseproject.data.mappers.toEntity
import com.addenisov00.courseproject.data.mappers.toMessageItem
import com.addenisov00.courseproject.data.network.EditLateMessageException
import com.addenisov00.courseproject.data.network.EditMessageException
import com.addenisov00.courseproject.data.network.MessageApiClient
import com.addenisov00.courseproject.data.network.MessageException
import com.addenisov00.courseproject.data.network.models.toMessageItem
import com.addenisov00.courseproject.domain.MessagesRepository
import com.addenisov00.courseproject.presentation.messenger.models.MessageContent
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import retrofit2.HttpException

class MessagesRepositoryImpl(
    private val api: MessageApiClient,
    private val dao: MessagesDao,
    private val htmlHelper: HtmlHelper = HtmlHelperImpl()
) : MessagesRepository {

    override suspend fun loadMessages(
        numBefore: Int,
        numAfter: Int,
        anchor: Any,
        currentItem: InfoForMessenger
    ) {

        val narrow = when (currentItem) {
            is InfoForMessenger.Channel ->
                NarrowUtils.createNarrow(currentItem.ChannelItem.name)

            is InfoForMessenger.Topic -> {
                NarrowUtils.createNarrow(
                    currentItem.TopicItem.channelName,
                    currentItem.TopicItem.name
                )
            }
        }

        val result = api.getMessagesWithFilter(numBefore, numAfter, anchor, narrow)
        if (result.result == Constants.RESULT_SUCCESS) {
            val messageList = result.messages.map { it.toMessageItem(htmlHelper) }
            dao.insertMessages(messageList.map { it.toEntity(currentItem) })
        } else
            throw MessageException(result.message)

    }

    override fun getMessagesFromCache(currentItem: InfoForMessenger): Flow<List<MessageItem>> {

        return when (currentItem) {
            is InfoForMessenger.Channel -> {
                dao.getMessagesFromChannel(currentItem.ChannelItem.id).map { messages ->
                    messages.map {
                        it.toMessageItem()
                    }
                }
            }

            is InfoForMessenger.Topic -> {
                dao.getMessagesFromTopic(currentItem.TopicItem.uniqueName).map { messages ->
                    messages.map {
                        it.toMessageItem()
                    }
                }
            }
        }
    }

    override suspend fun addReaction(
        messageId: Int,
        emojiName: String,
        currentItem: InfoForMessenger
    ) {
        val result = api.addReaction(messageId, emojiName)
        if (result.result == Constants.RESULT_SUCCESS) {
            val newMessage = api.getMessage(messageId)
            if (newMessage.result == Constants.RESULT_SUCCESS) {
                dao.insertMessages(
                    listOf(
                        newMessage.receivedMessage.toMessageItem(htmlHelper).toEntity(currentItem)
                    )
                )
                return
            } else
                throw EditMessageException(newMessage.message)
        } else
            throw EditMessageException(result.message)
    }


    override suspend fun deleteReaction(
        messageId: Int,
        emojiName: String,
        currentItem: InfoForMessenger
    ) {
        val result = api.deleteReaction(messageId, emojiName)
        if (result.result == Constants.RESULT_SUCCESS) {
            val newMessage = api.getMessage(messageId)
            if (newMessage.result == Constants.RESULT_SUCCESS) {
                dao.insertMessages(
                    listOf(
                        newMessage.receivedMessage.toMessageItem(htmlHelper).toEntity(currentItem)
                    )
                )
                return
            } else
                throw EditMessageException(newMessage.message)
        } else
            throw EditMessageException(result.message)
    }


    override suspend fun deleteMessage(messageId: Int) {
        val result = api.deleteMessage(messageId)
        if (result.result == Constants.RESULT_SUCCESS) {
            dao.deleteMessage(messageId)
            return
        } else
            throw EditMessageException(result.message)
    }


    override suspend fun editMessage(messageId: Int, newContent: String) {
        try {
            val result = api.editMessage(messageId, newContent)
            when (result.result) {
                Constants.RESULT_SUCCESS -> {
                    dao.editMessage(messageId, MessageContent(text = newContent, imageUrl = ""))
                    return
                }

                else ->
                    throw EditMessageException(result.message)
            }
        } catch (e: HttpException) {
            if (e.code() == Constants.BAD_REQUEST_ERROR)
                throw EditLateMessageException
        } catch (e: Throwable) {
            throw e
        }
    }


    override suspend fun sendMessage(
        content: String,
        currentItem: InfoForMessenger
    ) {
        val result = when (currentItem) {
            is InfoForMessenger.Channel -> api.sendMessageToTopic(
                Constants.TYPE_STREAM,
                currentItem.getChannelId(),
                content
            )

            is InfoForMessenger.Topic -> api.sendMessageToTopic(
                Constants.TYPE_STREAM,
                currentItem.getChannelId(),
                content,
                currentItem.TopicItem.name
            )
        }

        if (result.result == Constants.RESULT_SUCCESS) {
            val newMessage = api.getMessage(result.id)
            if (newMessage.result == Constants.RESULT_SUCCESS) {
                dao.insertMessages(
                    listOf(
                        newMessage.receivedMessage.toMessageItem(htmlHelper).toEntity(currentItem)
                    )
                )
                return
            } else
                throw EditMessageException(newMessage.message)
        } else
            throw EditMessageException(result.message)
    }

    override suspend fun addPhoto(
        file: Uri,
        currentItem: InfoForMessenger,
        resolver: ContentResolver
    ) {
        val contentPart = InputStreamRequestBody("image/*".toMediaType(), resolver, file)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("something", "image.jpg", contentPart)
            .build()
        val result = api.addPhoto(requestBody)
        if (result.result == Constants.RESULT_SUCCESS) {
            sendMessage("[](${result.uri})", currentItem)
        } else
            throw MessageException(result.message)
    }

    override suspend fun loadLastMessages(currentItem: InfoForMessenger) {
        val narrow = when (currentItem) {
            is InfoForMessenger.Channel ->
                NarrowUtils.createNarrow(currentItem.ChannelItem.name)

            is InfoForMessenger.Topic -> {
                NarrowUtils.createNarrow(
                    currentItem.TopicItem.channelName,
                    currentItem.TopicItem.name
                )
            }
        }
        val result = api.getMessagesWithFilter(
            numAfter = 0,
            numBefore = Constants.MESSAGE_FIRST_COUNT,
            anchor = Constants.LAST_MESSAGES,
            narrow = narrow
        )
        if (result.result == Constants.RESULT_SUCCESS) {
            when (currentItem) {
                is InfoForMessenger.Channel -> {
                    dao.clearMessagesFromChannel(currentItem.getChannelId())
                }

                is InfoForMessenger.Topic -> {
                    val topicUniqueName = "${currentItem.getName()}${currentItem.getChannelId()}"
                    dao.clearMessagesFromTopic(topicUniqueName)
                }
            }

            dao.insertMessages(result.messages.map {
                it.toMessageItem(htmlHelper).toEntity(currentItem)
            })

        } else
            throw MessageException(result.message)
    }
}


