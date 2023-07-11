package com.addenisov00.courseproject.presentation.main_screen.channels.models

import android.os.Parcelable
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChannelItem(
    val name: String,
    val id: Int,
    val isExpand: Boolean
) : DelegateItem, Parcelable {

    override fun content(): Any = this

    override fun id() = id

    override fun compareToOther(other: DelegateItem) =
        (other as ChannelItem).content() == this.content()
}
