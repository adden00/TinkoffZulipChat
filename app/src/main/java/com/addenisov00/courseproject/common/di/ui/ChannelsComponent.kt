package com.addenisov00.courseproject.common.di.ui

import com.addenisov00.courseproject.common.di.AppComponent
import com.addenisov00.courseproject.common.di.ScreenScope
import com.addenisov00.courseproject.presentation.main_screen.channels.ChannelsListFragment
import com.addenisov00.courseproject.presentation.main_screen.channels.ChannelsViewModel
import dagger.Component

@ScreenScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ChannelsModule::class, ChannelsBindModule::class]
)
interface ChannelsComponent {
    fun inject(fragment: ChannelsListFragment)
    fun inject(viewModel: ChannelsViewModel)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): ChannelsComponent
    }
}

