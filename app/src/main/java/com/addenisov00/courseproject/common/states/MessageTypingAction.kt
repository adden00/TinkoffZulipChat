package com.addenisov00.courseproject.common.states

sealed class MessageTypingAction {
    object Creating : MessageTypingAction()
    data class Editing(val messageId: Int) : MessageTypingAction()
}