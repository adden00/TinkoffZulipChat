package com.addenisov00.courseproject.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.addenisov00.courseproject.presentation.messenger.models.MessageContent

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "message")
    val message: MessageContent,

    @ColumnInfo(name = "reactions")
    val reactions: List<ReactionEntity>,

    @ColumnInfo(name = "is_my")
    val isMy: Boolean,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long,

    @ColumnInfo(name = "topic_unique_name")
    val topicUniqueName: String?,

    @ColumnInfo(name = "channel_id")
    val channelID: Int
)