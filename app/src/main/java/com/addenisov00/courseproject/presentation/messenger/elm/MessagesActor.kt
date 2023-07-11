package com.addenisov00.courseproject.presentation.messenger.elm

import com.addenisov00.courseproject.domain.MessagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

class MessagesActor @Inject constructor(private val repository: MessagesRepository) :
    Actor<MessageCommand, MessagesEvent> {
    private val switcher = Switcher()
    override fun execute(command: MessageCommand): Flow<MessagesEvent> =
        when (command) {

            is MessageCommand.AddReaction -> {
                switcher.switch {
                    flow {
                        emit(
                            repository.addReaction(
                                command.messageId,
                                command.reactionName,
                                currentItem = command.currentItem
                            )
                        )
                    }
                }.mapEvents(
                    { MessagesEvent.Internal.ReactionChanged },
                    { MessagesEvent.Internal.Error }
                )
            }

            is MessageCommand.DeleteReaction -> {
                switcher.switch {
                    flow {
                        emit(
                            repository.deleteReaction(
                                command.messageId,
                                command.reactionName,
                                command.currentItem
                            )
                        )
                    }
                }.mapEvents(
                    { MessagesEvent.Internal.ReactionChanged },
                    { MessagesEvent.Internal.Error }
                )
            }

            is MessageCommand.SubscribeOnMessages -> {
                repository.getMessagesFromCache(command.currentItem)
                    .mapEvents(
                        { event -> MessagesEvent.Internal.MessagesLoadedFromCache(event) },
                        { MessagesEvent.Internal.ErrorWithDatabase }
                    )
            }

            is MessageCommand.LoadMoreMessages -> {
                switcher.switch {
                    flow {
                        emit(
                            repository.loadMessages(
                                command.numBefore,
                                command.numAfter,
                                command.anchor,
                                command.currentItem
                            )
                        )
                    }
                }.mapEvents(
                    { MessagesEvent.Internal.MoreMessagesLoaded },
                    { MessagesEvent.Internal.Error }
                )
            }

            is MessageCommand.SendMessage -> {
                switcher.switch {
                    flow {
                        emit(
                            repository.sendMessage(
                                command.content,
                                command.currentItem
                            )
                        )
                    }
                }.mapEvents(
                    { MessagesEvent.Internal.MessageSent(command.currentItem) },
                    { MessagesEvent.Internal.ErrorWhileMessageSending }
                )
            }

            is MessageCommand.DeleteMessage -> {
                switcher.switch {
                    flow {
                        emit(repository.deleteMessage(command.messageId))
                    }
                }.mapEvents(
                    { MessagesEvent.Internal.MessageDeleted },
                    { MessagesEvent.Internal.ErrorWhileDeleting }
                )
            }

            is MessageCommand.EditMessage -> {
                switcher.switch {
                    flow {
                        emit(repository.editMessage(command.messageId, command.newContent))
                    }
                }.mapEvents(
                    { MessagesEvent.Internal.MessageEdited },
                    { error -> MessagesEvent.Internal.ErrorWhileEditing(error) }
                )
            }

            is MessageCommand.SendPicture -> {
                switcher.switch {
                    flow {
                        emit(
                            repository.addPhoto(
                                command.picture,
                                command.currentItem,
                                command.contentResolver
                            )
                        )
                    }
                }.mapEvents(
                    { MessagesEvent.Internal.MessageSent(command.currentItem) },
                    { MessagesEvent.Internal.ErrorWhileMessageSending }
                )
            }

            is MessageCommand.LoadLastMessages -> {
                switcher.switch {
                    flow {
                        emit(
                            repository.loadLastMessages(command.currentItem)
                        )
                    }
                }.mapEvents(
                    { MessagesEvent.Internal.FirstMessagesLoaded },
                    { MessagesEvent.Internal.Error }
                )
            }
        }
}

