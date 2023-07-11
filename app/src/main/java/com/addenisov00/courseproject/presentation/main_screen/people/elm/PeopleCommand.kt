package com.addenisov00.courseproject.presentation.main_screen.people.elm


sealed class PeopleCommand {
    object LoadPeople : PeopleCommand()
    data class LoadPerson(val id: Int) : PeopleCommand()
}