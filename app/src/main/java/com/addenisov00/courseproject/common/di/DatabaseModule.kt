package com.addenisov00.courseproject.common.di

import android.content.Context
import androidx.room.Room
import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.data.local.MessengerDatabase
import com.addenisov00.courseproject.data.local.dao.ChannelsDao
import com.addenisov00.courseproject.data.local.dao.MessagesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MessengerDatabase =
        Room.databaseBuilder(
            context,
            MessengerDatabase::class.java,
            Constants.MESSENGER_DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideChannelsDao(database: MessengerDatabase): ChannelsDao =
        database.getChannelsDao()

    @Provides
    @Singleton
    fun provideMessagesDao(database: MessengerDatabase): MessagesDao =
        database.getMessagesDao()

}