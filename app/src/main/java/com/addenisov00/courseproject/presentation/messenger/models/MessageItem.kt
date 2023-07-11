package com.addenisov00.courseproject.presentation.messenger.models

import android.os.Parcelable
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageItem(
    val id: Int,
    val userName: String,
    val userId: Int,
    val content: MessageContent,
    val reactions: List<ReactionItem>,
    val isMy: Boolean,
    val imageUrl: String,
    val timeStamp: Long
) : DelegateItem, Parcelable {

    override fun id() = id
    override fun content(): Any = this
    override fun compareToOther(other: DelegateItem) =
        (other as MessageItem).content() == this.content()
}
