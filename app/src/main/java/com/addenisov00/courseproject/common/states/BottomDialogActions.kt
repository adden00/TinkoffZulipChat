package com.addenisov00.courseproject.common.states

import com.addenisov00.courseproject.presentation.messenger.models.EmojiListItem

sealed class BottomDialogActions {
    data class AddReaction(val item: EmojiListItem) : BottomDialogActions()
    object CopyMessage : BottomDialogActions()
    object EditMessage : BottomDialogActions()
    object DeleteMessage : BottomDialogActions()
}