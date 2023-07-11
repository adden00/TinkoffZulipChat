package com.addenisov00.courseproject.presentation.main_screen.people.elm

sealed class PeopleEffect {
    data class Error(val e: Throwable) : PeopleEffect()
    object ShowLoader : PeopleEffect()
    object HideLoader : PeopleEffect()
}