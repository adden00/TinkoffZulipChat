package com.addenisov00.courseproject.presentation.messenger.elm

import android.content.ContentResolver
import android.net.Uri
import com.addenisov00.courseproject.common.Constants.MESSAGE_PAGE_COUNT
import com.addenisov00.courseproject.common.states.InfoForMessenger

sealed class MessageCommand {
    data class SubscribeOnMessages(
        val currentItem: InfoForMessenger,
    ) : MessageCommand()

    data class LoadMoreMessages(
        val numBefore: Int = MESSAGE_PAGE_COUNT,
        val numAfter: Int = 0,
        val anchor: Any = "newest",
        val currentItem: InfoForMessenger
    ) : MessageCommand()

    data class LoadLastMessages(
        val currentItem: InfoForMessenger
    ) : MessageCommand()

    data class AddReaction(
        val messageId: Int,
        val reactionName: String,
        val currentItem: InfoForMessenger

    ) : MessageCommand()

    data class DeleteReaction(
        val messageId: Int,
        val reactionName: String,
        val currentItem: InfoForMessenger

    ) : MessageCommand()

    data class SendMessage(
        val content: String,
        val currentItem: InfoForMessenger
    ) : MessageCommand()


    data class DeleteMessage(
        val messageId: Int
    ) : MessageCommand()

    data class EditMessage(
        val messageId: Int,
        val newContent: String
    ) : MessageCommand()

    data class SendPicture(
        val picture: Uri,
        val currentItem: InfoForMessenger,
        val contentResolver: ContentResolver
    ) : MessageCommand()

}