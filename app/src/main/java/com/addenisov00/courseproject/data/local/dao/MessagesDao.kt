package com.addenisov00.courseproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.addenisov00.courseproject.data.local.entities.MessageEntity
import com.addenisov00.courseproject.presentation.messenger.models.MessageContent
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Query("SELECT * FROM messages WHERE topic_unique_name LIKE :topicUniqueName")
    fun getMessagesFromTopic(topicUniqueName: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMessage(message: MessageEntity)

    @Query("UPDATE messages SET message = :newContent WHERE id LIKE :messageId")
    suspend fun editMessage(messageId: Int, newContent: MessageContent)

    @Query("SELECT * FROM messages WHERE channel_id LIKE :channelId")
    fun getMessagesFromChannel(channelId: Int): Flow<List<MessageEntity>>

    @Query("DELETE FROM messages WHERE id LIKE :messageId")
    suspend fun deleteMessage(messageId: Int)

    @Query("DELETE FROM messages WHERE topic_unique_name LIKE :topicName")
    suspend fun clearMessagesFromTopic(topicName: String)

    @Query("DELETE FROM messages WHERE channel_id LIKE :channelId")
    suspend fun clearMessagesFromChannel(channelId: Int)


}