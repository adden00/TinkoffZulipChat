package com.addenisov00.courseproject.common.di

import com.addenisov00.courseproject.data.local.dao.ChannelsDao
import com.addenisov00.courseproject.data.local.dao.MessagesDao
import com.addenisov00.courseproject.data.network.MessageApiClient
import com.addenisov00.courseproject.data.repository.ChannelsRepositoryImpl
import com.addenisov00.courseproject.data.repository.MessagesRepositoryImpl
import com.addenisov00.courseproject.data.repository.UsersRepositoryImpl
import com.addenisov00.courseproject.domain.ChannelsRepository
import com.addenisov00.courseproject.domain.MessagesRepository
import com.addenisov00.courseproject.domain.UsersRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideMessageRepository(api: MessageApiClient, dao: MessagesDao): MessagesRepository =
        MessagesRepositoryImpl(api, dao)

    @Provides
    fun provideChannelsRepository(api: MessageApiClient, dao: ChannelsDao): ChannelsRepository =
        ChannelsRepositoryImpl(api, dao)

    @Provides
    fun provideUsersRepository(api: MessageApiClient): UsersRepository =
        UsersRepositoryImpl(api)
}

