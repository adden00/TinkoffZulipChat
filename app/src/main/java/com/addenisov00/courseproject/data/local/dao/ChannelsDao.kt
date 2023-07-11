package com.addenisov00.courseproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.addenisov00.courseproject.data.local.entities.AllChannelEntity
import com.addenisov00.courseproject.data.local.entities.MyChannelEntity
import com.addenisov00.courseproject.data.local.entities.TopicEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ChannelsDao {

    @Query("SELECT * FROM my_channels")
    fun getMyChannels(): Flow<List<MyChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyChannels(channels: List<MyChannelEntity>)

    @Query("DELETE FROM my_channels")
    suspend fun deleteMyChannels()

    @Query("DELETE FROM all_channels")
    suspend fun deleteAllChannels()

    @Query("SELECT * FROM all_channels")
    fun getAllChannels(): Flow<List<AllChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllChannels(channels: List<AllChannelEntity>)

    @Query("SELECT * FROM topics WHERE channel_id LIKE :channelId")
    suspend fun getTopicsFromChannel(channelId: Int): List<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopicsToChannel(topicsList: List<TopicEntity>)

}
