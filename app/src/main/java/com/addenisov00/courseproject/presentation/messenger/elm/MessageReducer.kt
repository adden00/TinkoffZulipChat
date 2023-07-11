package com.addenisov00.courseproject.presentation.messenger.elm

import com.addenisov00.courseproject.common.states.MessageInputType
import com.addenisov00.courseproject.common.states.MessageTypingAction
import com.addenisov00.courseproject.common.states.ScrollType
import com.addenisov00.courseproject.data.network.EditLateMessageException
import com.addenisov00.courseproject.presentation.utills.addTimeStamps
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

object MessageReducer : DslReducer<MessagesEvent, MessageState, MessageEffect, MessageCommand>() {
    override fun Result.reduce(event: MessagesEvent): Any =
        when (event) {
            is MessagesEvent.Internal.Error -> {
                effects { +MessageEffect.ShowError }
                effects { +MessageEffect.HideLoader }
            }

            is MessagesEvent.Internal.MessagesLoadedFromCache -> {
                effects { +MessageEffect.HideDatabaseError }
                state { copy(currentList = event.messageList.addTimeStamps()) }
            }

            is MessagesEvent.Internal.MoreMessagesLoaded -> {
                effects { +MessageEffect.HideLoader }
                state { copy(currentScroll = ScrollType.UP) }
            }

            is MessagesEvent.Internal.MessageDeleted -> {
                effects { +MessageEffect.HideLoader }
                effects { +MessageEffect.ShowSuccessDeleted }
            }

            is MessagesEvent.Internal.ErrorWhileDeleting -> {
                effects { +MessageEffect.HideLoader }
                effects { +MessageEffect.ShowErrorWhileDeleted }
            }

            is MessagesEvent.Internal.MessageSent -> {
                effects { +MessageEffect.HideMessageLoader }
                state { copy(currentScroll = ScrollType.DOWN) }
            }

            is MessagesEvent.Internal.ErrorWhileMessageSending -> {
                effects { +MessageEffect.HideMessageLoader }
                effects { +MessageEffect.ShowError }
            }

            is MessagesEvent.Internal.ErrorWhileEditing -> {
                state { copy(currentAction = MessageTypingAction.Creating) }
                effects { +MessageEffect.HideMessageLoader }

                if (event.e is EditLateMessageException) {
                    effects { +MessageEffect.ShowLateErrorWhileEdited }
                } else
                    effects { +MessageEffect.ShowErrorWhileEdited }
            }

            is MessagesEvent.Internal.MessageEdited -> {
                state { copy(currentAction = MessageTypingAction.Creating) }
                effects { +MessageEffect.HideMessageLoader }
                effects { +MessageEffect.ShowSuccessEdited }
            }

            is MessagesEvent.Internal.ReactionChanged -> {
                effects { +MessageEffect.HideMessageLoader }
            }

            is MessagesEvent.Internal.FirstMessagesLoaded -> {
                effects { +MessageEffect.HideLoader }
                state { copy(currentScroll = ScrollType.DOWN) }

            }

            is MessagesEvent.Internal.ErrorWithDatabase -> {
                effects { +MessageEffect.ShowDatabaseError }
                effects { +MessageEffect.HideLoader }

            }
//-------------------------------------UI-----------------------------------


            is MessagesEvent.Ui.Init -> {
                effects { +MessageEffect.HideDatabaseError }
                state { copy(currentItem = event.currentChannelAndTopic) }
                commands { +MessageCommand.SubscribeOnMessages(event.currentChannelAndTopic) }
                effects { +MessageEffect.ShowLoader }
                commands { +MessageCommand.LoadLastMessages(currentItem = event.currentChannelAndTopic) }

            }

            is MessagesEvent.Ui.AddReaction -> {
                commands {
                    +MessageCommand.AddReaction(
                        event.messageId,
                        event.emojiName,
                        event.currentItem
                    )
                }
            }


            is MessagesEvent.Ui.DeleteReaction -> {
                commands {
                    +MessageCommand.DeleteReaction(
                        event.messageId,
                        event.emojiName,
                        event.currentItem
                    )
                }
            }


            is MessagesEvent.Ui.LoadMoreMessages -> {
                effects { +MessageEffect.ShowLoader }
                commands {
                    +MessageCommand.LoadMoreMessages(
                        event.numBefore,
                        event.numAfter,
                        event.anchor,
                        event.currentItem
                    )
                }
                state { copy(paginationAnchor = event.holdPaginationPosition) }
            }


            is MessagesEvent.Ui.LoadMessagesFromCache -> {
                commands { +MessageCommand.SubscribeOnMessages(event.currentItem) }
            }

            is MessagesEvent.Ui.DeleteMessage -> {
                effects { +MessageEffect.ShowLoader }
                commands { +MessageCommand.DeleteMessage(event.messageId) }
            }


            is MessagesEvent.Ui.StartEditing -> {
                effects { +MessageEffect.PutMessageToEditText(event.messageItem.content.text) }
                state { copy(currentAction = MessageTypingAction.Editing(event.messageItem.id)) }
            }

            is MessagesEvent.Ui.SendPicture -> {
                effects { +MessageEffect.ShowMessageLoader }
                commands {
                    +MessageCommand.SendPicture(
                        event.picture,
                        event.currentItem,
                        event.contentResolver
                    )
                }
            }


            is MessagesEvent.Ui.ButtonSendClicked -> {

                when (state.currentInputType) {
                    MessageInputType.TEXT -> {
                        when (val currentAction = state.currentAction) {
                            is MessageTypingAction.Creating -> {
                                effects { +MessageEffect.ShowMessageLoader }
                                commands {
                                    +MessageCommand.SendMessage(
                                        event.text,
                                        event.currentItem
                                    )
                                }

                            }

                            is MessageTypingAction.Editing -> {
                                effects { +MessageEffect.ShowMessageLoader }
                                commands {
                                    +MessageCommand.EditMessage(
                                        currentAction.messageId,
                                        event.text
                                    )
                                }
                            }
                        }
                    }

                    MessageInputType.FILE -> {
                        effects { +MessageEffect.OpenPhotoPicker }
                    }
                }
                effects { +MessageEffect.ClearInputText }
            }

            is MessagesEvent.Ui.InputTextChanged -> {
                if (event.text.isEmpty()) {
                    state { copy(currentInputType = MessageInputType.FILE) }
                    effects { +MessageEffect.SetSendButtonLogo(MessageInputType.FILE) }
                } else {
                    state { copy(currentInputType = MessageInputType.TEXT) }
                    effects { +MessageEffect.SetSendButtonLogo(MessageInputType.TEXT) }

                }
            }

            is MessagesEvent.Ui.ItemsInserted -> {
                when (state.currentScroll) {
                    ScrollType.DOWN -> {
                        effects { +MessageEffect.ScrollDown }
                        state { copy(currentScroll = ScrollType.NONE) }
                    }

                    ScrollType.NONE -> Unit
                    ScrollType.UP -> {
                        effects { +MessageEffect.ScrollToMessage(state.paginationAnchor) }
                        state { copy(currentScroll = ScrollType.NONE) }
                    }
                }
            }
        }
}