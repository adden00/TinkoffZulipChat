package com.addenisov00.courseproject.common.di.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addenisov00.courseproject.common.di.ViewModelKey
import com.addenisov00.courseproject.domain.MessagesRepository
import com.addenisov00.courseproject.presentation.messenger.MessageViewModel
import com.addenisov00.courseproject.presentation.messenger.MessengerViewModelFactory
import com.addenisov00.courseproject.presentation.messenger.elm.MessagesActor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class MessengerModule {
    @Provides
    fun provideMessagesActor(repository: MessagesRepository): MessagesActor =
        MessagesActor(repository)
}

@Module
interface MessengerBindModule {

    @Binds
    fun bindViewModelFactory(impl: MessengerViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @ViewModelKey(MessageViewModel::class)
    @Binds
    fun bindMessengerViewModel(impl: MessageViewModel): ViewModel
}