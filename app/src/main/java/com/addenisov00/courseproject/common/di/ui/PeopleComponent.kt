package com.addenisov00.courseproject.common.di.ui

import com.addenisov00.courseproject.common.di.AppComponent
import com.addenisov00.courseproject.common.di.ScreenScope
import com.addenisov00.courseproject.presentation.main_screen.people.PeopleFragment
import com.addenisov00.courseproject.presentation.main_screen.people.PeopleStoreViewModel
import com.addenisov00.courseproject.presentation.main_screen.people.ProfileFragment
import dagger.Component

@ScreenScope
@Component(
    dependencies = [AppComponent::class],
    modules = [PeopleModule::class, PeopleBindModule::class]
)
interface PeopleComponent {
    fun inject(fragment: PeopleFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(viewModel: PeopleStoreViewModel)


    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): PeopleComponent
    }
}

