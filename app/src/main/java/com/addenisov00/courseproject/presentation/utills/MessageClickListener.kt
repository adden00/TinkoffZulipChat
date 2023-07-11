package com.addenisov00.courseproject.presentation.utills

import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import com.addenisov00.courseproject.presentation.messenger.models.ReactionItem

interface MessageClickListener {
    fun onMessageClick(message: MessageItem)
    fun onReactionClick(message: MessageItem, reaction: ReactionItem)
}