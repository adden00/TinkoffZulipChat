package com.addenisov00.courseproject.common.di.ui

import com.addenisov00.courseproject.common.di.AppComponent
import com.addenisov00.courseproject.common.di.ScreenScope
import com.addenisov00.courseproject.presentation.messenger.MessageViewModel
import com.addenisov00.courseproject.presentation.messenger.MessengerFragment
import dagger.Component

@ScreenScope
@Component(dependencies = [AppComponent::class], modules = [MessengerBindModule::class])
interface MessageComponent {

    fun inject(fragment: MessengerFragment)
    fun inject(viewModel: MessageViewModel)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): MessageComponent
    }
}

