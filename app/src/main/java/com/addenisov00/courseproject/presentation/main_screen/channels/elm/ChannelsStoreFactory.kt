package com.addenisov00.courseproject.presentation.main_screen.channels.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

object ChannelsStoreFactory {
    fun getStore(actor: ChannelsActor) =
        ElmStoreCompat(
            ChannelsState(),
            ChannelsReducer,
            actor
        )
}


