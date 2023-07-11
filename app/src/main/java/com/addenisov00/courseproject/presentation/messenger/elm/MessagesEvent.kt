package com.addenisov00.courseproject.presentation.messenger.elm

import android.content.ContentResolver
import android.net.Uri
import com.addenisov00.courseproject.common.Constants.MESSAGE_PAGE_COUNT
import com.addenisov00.courseproject.common.states.InfoForMessenger
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem

sealed class MessagesEvent {
    sealed class Internal : MessagesEvent() {
        object MoreMessagesLoaded : Internal()
        object Error : Internal()
        object ReactionChanged : Internal()
        object ErrorWhileMessageSending : Internal()
        object MessageDeleted : Internal()
        object ErrorWhileDeleting : Internal()
        object MessageEdited : Internal()
        object FirstMessagesLoaded : Internal()
        object ErrorWithDatabase : Internal()
        data class MessagesLoadedFromCache(val messageList: List<MessageItem>) : Internal()
        data class MessageSent(val currentItem: InfoForMessenger) : Internal()
        data class ErrorWhileEditing(val e: Throwable) : Internal()


    }

    sealed class Ui : MessagesEvent() {
        data class Init(val currentChannelAndTopic: InfoForMessenger) : Ui()
        data class LoadMessagesFromCache(
            val currentItem: InfoForMessenger
        ) : Ui()

        data class LoadMoreMessages(
            val numBefore: Int = MESSAGE_PAGE_COUNT,
            val numAfter: Int = 0,
            val anchor: Any = "newest",
            val currentItem: InfoForMessenger,
            val holdPaginationPosition: Int
        ) : Ui()

        data class AddReaction(
            val messageId: Int,
            val emojiName: String,
            val currentItem: InfoForMessenger
        ) : Ui()

        data class DeleteReaction(
            val messageId: Int,
            val emojiName: String,
            val currentItem: InfoForMessenger
        ) : Ui()

        data class DeleteMessage(
            val messageId: Int
        ) : Ui()

        data class StartEditing(
            val messageItem: MessageItem
        ) : Ui()

        data class SendPicture(
            val picture: Uri,
            val currentItem: InfoForMessenger,
            val contentResolver: ContentResolver
        ) : Ui()

        data class ButtonSendClicked(
            val text: String,
            val currentItem: InfoForMessenger
        ) : Ui()

        data class InputTextChanged(val text: String) : Ui()

        object ItemsInserted : Ui()


//        object ListSubmitted: Ui()

    }

}