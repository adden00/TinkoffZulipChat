package com.addenisov00.courseproject.presentation.main_screen.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleActor
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleStoreFactory
import javax.inject.Inject
import javax.inject.Provider

class PeopleStoreViewModel @Inject constructor(private val actor: PeopleActor) : ViewModel() {
    private val peopleStore by lazy { PeopleStoreFactory.getStore(actor) }
    fun peopleStore() = peopleStore
}


@Suppress("UNCHECKED_CAST")
open class PeopleStoreViewModelFactory @Inject constructor(
    private val viewModels: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModels[modelClass]
            ?: error("ViewModel class $modelClass not found")
        return viewModelProvider.get() as T
    }
}