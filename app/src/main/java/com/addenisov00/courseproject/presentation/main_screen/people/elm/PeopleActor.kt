package com.addenisov00.courseproject.presentation.main_screen.people.elm

import com.addenisov00.courseproject.domain.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch

class PeopleActor(private val repository: UsersRepository) : Actor<PeopleCommand, PeopleEvent> {
    private val switcher = Switcher()


    override fun execute(command: PeopleCommand): Flow<PeopleEvent> =
        when (command) {
            is PeopleCommand.LoadPerson -> switcher.switch { personItemFlow(command.id) }
                .mapEvents(
                    { personItem -> PeopleEvent.Internal.PersonItemLoaded(personItem) },
                    PeopleEvent.Internal::Error
                )
            PeopleCommand.LoadPeople -> switcher.switch { allPeopleFlow() }
                .mapEvents(
                    { model -> PeopleEvent.Internal.PeopleLoaded(model) },
                    { error ->
                        run {
                            PeopleEvent.Internal.Error(error)
                        }
                    }
                )
        }

    private fun personItemFlow(id: Int) = flow {
        val result = repository.getUser(id)
        emit(result)
    }

    private fun allPeopleFlow() = flow {
        val result = repository.getAllUsers()
        emit(result)
    }
}