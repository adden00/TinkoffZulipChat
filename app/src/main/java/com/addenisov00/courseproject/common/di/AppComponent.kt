package com.addenisov00.courseproject.common.di

import android.content.Context
import com.addenisov00.courseproject.domain.ChannelsRepository
import com.addenisov00.courseproject.domain.MessagesRepository
import com.addenisov00.courseproject.domain.UsersRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, DatabaseModule::class])
interface AppComponent {
    fun messageRepository(): MessagesRepository
    fun usersRepository(): UsersRepository
    fun channelsRepository(): ChannelsRepository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context
        ): AppComponent
    }
}