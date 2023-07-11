package com.addenisov00.courseproject.presentation.main_screen.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addenisov00.courseproject.presentation.main_screen.channels.elm.ChannelsActor
import com.addenisov00.courseproject.presentation.main_screen.channels.elm.ChannelsStoreFactory
import javax.inject.Inject
import javax.inject.Provider

class ChannelsViewModel @Inject constructor(private val actor: ChannelsActor) : ViewModel() {
    private val channelStore by lazy { ChannelsStoreFactory.getStore(actor) }
    fun channelStore() = channelStore
}


@Suppress("UNCHECKED_CAST")
class ChannelsStoreViewModelFactory @Inject constructor(
    private val viewModels: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModels[modelClass]
            ?: error("ViewModel class $modelClass not found")
        return viewModelProvider.get() as T
    }
}