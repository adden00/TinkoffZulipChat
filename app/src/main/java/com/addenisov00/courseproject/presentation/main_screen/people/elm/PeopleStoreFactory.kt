package com.addenisov00.courseproject.presentation.main_screen.people.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

object PeopleStoreFactory {
    fun getStore(actor: PeopleActor) =
        ElmStoreCompat(
            PeopleState(),
            PeopleReducer,
            actor
        )
}


