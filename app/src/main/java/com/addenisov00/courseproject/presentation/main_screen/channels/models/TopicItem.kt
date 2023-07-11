package com.addenisov00.courseproject.presentation.main_screen.channels.models

import android.os.Parcelable
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicItem(
    val name: String,
    val channelId: Int,
    val channelName: String,
    val uniqueName: String = "$name$channelId"
) : DelegateItem, Parcelable {

    override fun content() = this
    override fun id(): Int = hashCode()
    override fun compareToOther(other: DelegateItem): Boolean =
        (other as? TopicItem)?.content() == this.content()

}
