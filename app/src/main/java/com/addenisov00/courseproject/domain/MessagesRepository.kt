package com.addenisov00.courseproject.domain


import android.content.ContentResolver
import android.net.Uri
import com.addenisov00.courseproject.common.states.InfoForMessenger
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {


    suspend fun addReaction(messageId: Int, emojiName: String, currentItem: InfoForMessenger)
    suspend fun deleteReaction(messageId: Int, emojiName: String, currentItem: InfoForMessenger)
    suspend fun sendMessage(
        content: String,
        currentItem: InfoForMessenger
    )

    suspend fun loadMessages(
        numBefore: Int,
        numAfter: Int,
        anchor: Any,
        currentItem: InfoForMessenger
    )

    fun getMessagesFromCache(currentItem: InfoForMessenger): Flow<List<MessageItem>>

    suspend fun deleteMessage(messageId: Int)
    suspend fun editMessage(messageId: Int, newContent: String)
    suspend fun addPhoto(
        file: Uri,
        currentItem: InfoForMessenger,
        resolver: ContentResolver
    )

    suspend fun loadLastMessages(currentItem: InfoForMessenger)
}