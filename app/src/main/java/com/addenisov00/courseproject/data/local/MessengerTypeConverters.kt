package com.addenisov00.courseproject.data.local

import androidx.room.TypeConverter
import com.addenisov00.courseproject.data.local.entities.ReactionEntity
import com.addenisov00.courseproject.presentation.messenger.models.MessageContent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class MessengerTypeConverters {

    @TypeConverter
    fun fromReaction(value: List<ReactionEntity>) = Json.encodeToString(value)

    @TypeConverter
    fun toReaction(value: String) = Json.decodeFromString<List<ReactionEntity>>(value)

    @TypeConverter
    fun fromMessageContent(value: MessageContent) = Json.encodeToString(value)

    @TypeConverter
    fun toMessageContent(value: String) = Json.decodeFromString<MessageContent>(value)

}
