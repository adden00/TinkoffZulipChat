package com.addenisov00.courseproject.presentation.main_screen.people.elm

import com.addenisov00.courseproject.presentation.main_screen.people.PersonItem

sealed class PeopleEvent {
    sealed class Internal : PeopleEvent() {
        class PersonItemLoaded(val person: PersonItem) : Internal()
        class PeopleLoaded(val peopleList: List<PersonItem>) : Internal()
        data class Error(val e: Throwable) : Internal()
    }

    sealed class Ui : PeopleEvent() {
        object Init : Ui()
        object LoadPeople : Ui()
        data class LoadPerson(val id: Int) : Ui()
        data class SearchPeople(val query: String) : Ui()
    }

}