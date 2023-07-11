package com.addenisov00.courseproject.presentation.messenger.elm

import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.common.states.InfoForMessenger
import com.addenisov00.courseproject.common.states.MessageInputType
import com.addenisov00.courseproject.common.states.MessageTypingAction
import com.addenisov00.courseproject.common.states.ScrollType

data class MessageState(
    val currentList: List<DelegateItem>? = null,
    val currentItem: InfoForMessenger? = null,
    val currentAction: MessageTypingAction = MessageTypingAction.Creating,
    val currentInputType: MessageInputType = MessageInputType.FILE,
    val currentScroll: ScrollType = ScrollType.NONE,
    val paginationAnchor: Int = 0
)