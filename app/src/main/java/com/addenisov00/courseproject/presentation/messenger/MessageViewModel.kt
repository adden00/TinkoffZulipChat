package com.addenisov00.courseproject.presentation.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addenisov00.courseproject.presentation.messenger.elm.MessageStoreFactory
import com.addenisov00.courseproject.presentation.messenger.elm.MessagesActor
import javax.inject.Inject
import javax.inject.Provider

class MessageViewModel @Inject constructor(actor: MessagesActor) : ViewModel() {

    private val messagesStore = MessageStoreFactory.createStore(actor)
    fun messagesStore() = messagesStore

}

@Suppress("UNCHECKED_CAST")
class MessengerViewModelFactory @Inject constructor(
    private val viewModels: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModels[modelClass]
            ?: error("ViewModel class $modelClass not found")
        return viewModelProvider.get() as T
    }
}