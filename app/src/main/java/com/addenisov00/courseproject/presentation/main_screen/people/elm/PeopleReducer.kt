package com.addenisov00.courseproject.presentation.main_screen.people.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

object PeopleReducer : DslReducer<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>() {
    override fun Result.reduce(event: PeopleEvent): Any = when (event) {

//---------------------Internal-------------------//

        is PeopleEvent.Internal.PersonItemLoaded -> {
            state { copy(currentPerson = event.person) }
            effects { +PeopleEffect.HideLoader }
        }

        is PeopleEvent.Internal.Error -> {
            effects { +PeopleEffect.HideLoader }
            effects { +PeopleEffect.Error(event.e) }
        }

        is PeopleEvent.Internal.PeopleLoaded -> {
            state { copy(peopleList = event.peopleList) }
            effects { +PeopleEffect.HideLoader }
        }

//-------------------------------UI-----------------------------------

        is PeopleEvent.Ui.Init -> Unit

        is PeopleEvent.Ui.LoadPeople -> {
            effects { +PeopleEffect.ShowLoader }
            commands { +PeopleCommand.LoadPeople }
        }

        is PeopleEvent.Ui.LoadPerson -> {
            effects { +PeopleEffect.ShowLoader }
            commands { +PeopleCommand.LoadPerson(event.id) }
        }

        is PeopleEvent.Ui.SearchPeople -> {
            if (event.query.isNotEmpty()) {
                state {
                    copy(
                        isSearching = true,
                        peopleSearchedList = peopleList?.filter {
                            event.query.trim().lowercase() in it.name.trim().lowercase()
                        } ?: listOf())
                }
            } else {
                state { copy(isSearching = false) }
            }
        }
    }
}