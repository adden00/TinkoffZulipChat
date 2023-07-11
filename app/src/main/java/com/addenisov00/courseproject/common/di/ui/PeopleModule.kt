package com.addenisov00.courseproject.common.di.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addenisov00.courseproject.common.di.ViewModelKey
import com.addenisov00.courseproject.domain.UsersRepository
import com.addenisov00.courseproject.presentation.main_screen.people.PeopleStoreViewModel
import com.addenisov00.courseproject.presentation.main_screen.people.PeopleStoreViewModelFactory
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleActor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class PeopleModule {
    @Provides
    fun providePeopleActor(repository: UsersRepository): PeopleActor = PeopleActor(repository)
}


@Module
interface PeopleBindModule {

    @Binds
    fun bindViewModelFactory(impl: PeopleStoreViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @ViewModelKey(PeopleStoreViewModel::class)
    @Binds
    fun bindMessengerViewModel(impl: PeopleStoreViewModel): ViewModel
}