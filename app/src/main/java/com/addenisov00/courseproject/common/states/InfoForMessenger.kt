package com.addenisov00.courseproject.common.states

import android.os.Parcelable
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class InfoForMessenger : Parcelable {
    abstract fun getName(): String
    abstract fun getChannelId(): Int

    data class Channel(val ChannelItem: ChannelItem) : InfoForMessenger() {
        override fun getName(): String = this.ChannelItem.name
        override fun getChannelId(): Int = this.ChannelItem.id
    }

    data class Topic(val TopicItem: TopicItem) : InfoForMessenger() {
        override fun getName(): String = this.TopicItem.name
        override fun getChannelId(): Int = this.TopicItem.channelId
    }
}