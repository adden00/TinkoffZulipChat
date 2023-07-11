package com.addenisov00.courseproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.addenisov00.courseproject.data.local.dao.ChannelsDao
import com.addenisov00.courseproject.data.local.dao.MessagesDao
import com.addenisov00.courseproject.data.local.entities.AllChannelEntity
import com.addenisov00.courseproject.data.local.entities.MessageEntity
import com.addenisov00.courseproject.data.local.entities.MyChannelEntity
import com.addenisov00.courseproject.data.local.entities.PersonEntity
import com.addenisov00.courseproject.data.local.entities.ReactionEntity
import com.addenisov00.courseproject.data.local.entities.TopicEntity

@Database(
    entities = [
        MyChannelEntity::class,
        AllChannelEntity::class,
        MessageEntity::class,
        PersonEntity::class,
        ReactionEntity::class,
        TopicEntity::class],
    version = 1
)
@TypeConverters(MessengerTypeConverters::class)
abstract class MessengerDatabase : RoomDatabase() {
    abstract fun getChannelsDao(): ChannelsDao
    abstract fun getMessagesDao(): MessagesDao
}