package com.addenisov00.courseproject.presentation.main_screen.people.elm

import com.addenisov00.courseproject.presentation.main_screen.people.PersonItem

data class PeopleState(
    val peopleList: List<PersonItem>? = null,
    val currentPerson: PersonItem? = null,
    val peopleSearchedList: List<PersonItem> = listOf(),
    val isSearching: Boolean = false
)