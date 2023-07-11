package com.addenisov00.courseproject.presentation.messenger.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

object MessageStoreFactory {
    fun createStore(actor: MessagesActor) =
        ElmStoreCompat(
            initialState = MessageState(),
            reducer = MessageReducer,
            actor = actor
        )
}