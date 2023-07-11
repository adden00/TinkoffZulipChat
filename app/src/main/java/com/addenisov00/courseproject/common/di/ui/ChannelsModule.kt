package com.addenisov00.courseproject.common.di.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addenisov00.courseproject.common.di.ViewModelKey
import com.addenisov00.courseproject.domain.ChannelsRepository
import com.addenisov00.courseproject.presentation.main_screen.channels.ChannelsStoreViewModelFactory
import com.addenisov00.courseproject.presentation.main_screen.channels.ChannelsViewModel
import com.addenisov00.courseproject.presentation.main_screen.channels.elm.ChannelsActor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ChannelsModule {
    @Provides
    fun provideChannelsActor(repository: ChannelsRepository): ChannelsActor =
        ChannelsActor(repository)
}

@Module
interface ChannelsBindModule {

    @Binds
    fun bindViewModelFactory(impl: ChannelsStoreViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @ViewModelKey(ChannelsViewModel::class)
    @Binds
    fun bindMessengerViewModel(impl: ChannelsViewModel): ViewModel
}