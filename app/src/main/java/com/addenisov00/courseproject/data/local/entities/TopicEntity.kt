package com.addenisov00.courseproject.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "channel_id")
    val channelId: Int,

    @ColumnInfo(name = "channel_name")
    val channelName: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "unique_name")
    val uniqueName: String
)