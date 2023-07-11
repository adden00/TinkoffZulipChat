package com.addenisov00.courseproject.presentation.main_screen.channels.elm

import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.common.states.ChannelClickType
import com.addenisov00.courseproject.common.states.ChannelCreateResult
import com.addenisov00.courseproject.common.states.ChannelsType
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

object ChannelsReducer :
    DslReducer<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>() {
    override fun Result.reduce(event: ChannelsEvent): Any = when (event) {

        //---------------------Internal-------------------//

        is ChannelsEvent.Internal.MyChannelsLoadedFromCache -> {
            state { copy(myChannelsList = event.myChannelsList) }
        }

        is ChannelsEvent.Internal.MyChannelsUpdated -> {
            effects { +ChannelsEffect.HideLoading }
            effects { +ChannelsEffect.HideCreatingLoader }
        }

        is ChannelsEvent.Internal.AllChannelsLoadedFromCache -> {
            state { copy(allChannelsList = event.allChannelsList) }
        }

        is ChannelsEvent.Internal.AllChannelsUpdated -> {
            effects { +ChannelsEffect.HideLoading }
            effects { +ChannelsEffect.HideCreatingLoader }
        }

        is ChannelsEvent.Internal.ErrorWhileChannelsLoading -> {
            effects { +ChannelsEffect.HideLoading }
            effects { +ChannelsEffect.ShowErrorWhileChannelsLoading }
        }

        is ChannelsEvent.Internal.ErrorWithDatabase -> {
            effects { +ChannelsEffect.ShowErrorWithDatabase }
        }

        is ChannelsEvent.Internal.TopicsLoadedFromCache -> {
            if (event.topicsList.isNotEmpty())
                when (event.channelsType) {
                    ChannelsType.MY_CHANNELS -> {
                        state {
                            copy(
                                myChannelsList = myChannelsList.addTopics(
                                    event.topicsList,
                                    event.channelItem
                                )
                            )
                        }
                    }

                    ChannelsType.ALL_CHANNELS -> {
                        state {
                            copy(
                                allChannelsList = allChannelsList.addTopics(
                                    event.topicsList,
                                    event.channelItem
                                )
                            )
                        }
                    }
                }
            effects { +ChannelsEffect.ShowTopicsLoader }
            commands { +ChannelsCommand.UpdateTopics(event.channelItem, event.channelsType) }
        }

        is ChannelsEvent.Internal.TopicsUpdated -> {
            effects { +ChannelsEffect.HideTopicsLoader }
            when (event.channelsType) {
                ChannelsType.MY_CHANNELS -> {
                    state {

                        copy(
                            myChannelsList = myChannelsList.addTopics(
                                event.topicsList,
                                event.channelItem
                            )
                        )
                    }
                }

                ChannelsType.ALL_CHANNELS -> {
                    state {
                        copy(
                            allChannelsList = allChannelsList.addTopics(
                                event.topicsList,
                                event.channelItem
                            )
                        )
                    }
                }
            }
        }

        is ChannelsEvent.Internal.ErrorWhileTopicsLoading -> {
            effects { +ChannelsEffect.HideTopicsLoader }
            effects { +ChannelsEffect.ShowErrorWhileTopicsLoading }
        }

        is ChannelsEvent.Internal.ChannelCreated -> {
            when (event.channelCreateResult) {
                ChannelCreateResult.CREATED ->
                    effects { +ChannelsEffect.ShowChannelCreated }

                ChannelCreateResult.ALREADY_EXIST ->
                    effects { +ChannelsEffect.ShowChannelAlreadyExist }
            }
            commands { +ChannelsCommand.UpdateMyChannels }
            commands { +ChannelsCommand.UpdateAllChannels }

        }


        is ChannelsEvent.Internal.ErrorWhileCreatingChannel -> {
            effects { +ChannelsEffect.HideCreatingLoader }
            effects { +ChannelsEffect.ShowErrorWhileCreating }
        }


        //---------------------Ui-------------------//

        is ChannelsEvent.Ui.Init -> {
            state { copy(currentChannelsType = event.channelsType) }
        }

        is ChannelsEvent.Ui.UpdateChannels -> {

            effects { +ChannelsEffect.ShowLoading }
            when (state.currentChannelsType) {

                ChannelsType.MY_CHANNELS -> {
                    commands { +ChannelsCommand.UpdateMyChannels }
                }

                ChannelsType.ALL_CHANNELS ->
                    commands { +ChannelsCommand.UpdateAllChannels }
            }
        }

        is ChannelsEvent.Ui.OnChannelClick -> {
            when (event.channelClickItem) {

                is ChannelClickType.ExpandArrowClicked -> {
                    if (event.channelClickItem.channelItem.isExpand)

                        when (state.currentChannelsType) {
                            ChannelsType.MY_CHANNELS -> {
                                state {
                                    copy(myChannelsList = myChannelsList.collapse())
                                }
                            }

                            ChannelsType.ALL_CHANNELS -> {
                                state { copy(allChannelsList = allChannelsList.collapse()) }
                            }
                        }
                    else
                        commands {
                            +ChannelsCommand.LoadTopics(
                                event.channelClickItem.channelItem,
                                state.currentChannelsType
                            )
                        }
                }

                is ChannelClickType.ChannelItemClicked ->
                    effects { +ChannelsEffect.OpenChannel(event.channelClickItem.channelItem) }
            }

        }


        is ChannelsEvent.Ui.SearchChannels -> {
            if (event.query.isEmpty())
                state { copy(isSearching = false) }
            else
                state { copy(isSearching = true) }

            state {
                copy(searchedChannels = currentList().collapse()
                    .filterIsInstance<ChannelItem>().filter {
                        event.query.lowercase() in it.name.lowercase()
                    })
            }
        }


        is ChannelsEvent.Ui.SubscribeOnChannels -> {
            when (state.currentChannelsType) {
                ChannelsType.MY_CHANNELS ->
                    commands { +ChannelsCommand.SubscribeOnMyChannels }

                ChannelsType.ALL_CHANNELS ->
                    commands { +ChannelsCommand.SubscribeOnAllChannels }
            }
        }


        is ChannelsEvent.Ui.CreateNewChannel -> {
            commands { +ChannelsCommand.CreateChannel(event.channelName, event.channelDescription) }
            effects { +ChannelsEffect.ShowCreatingLoader }
        }
    }
}

fun List<DelegateItem>?.addTopics(
    topicsList: List<DelegateItem>,
    channelItem: ChannelItem
): List<DelegateItem> {
    val editingList = this.collapse().toMutableList()
    var index = editingList.indexOfFirst { it.id() == channelItem.id }
    if (index != -1) {
        editingList[index] = (editingList[index] as ChannelItem).copy(isExpand = true)
        editingList.addAll(++index, topicsList)
    }
    return editingList.toList()
}

fun List<DelegateItem>?.collapse(): List<DelegateItem> {
    val editingList = (this ?: listOf()).filterIsInstance<ChannelItem>().toMutableList()
    val index = editingList.indexOfFirst { it.isExpand }
    if (index != -1)
        editingList[index] = (editingList[index]).copy(isExpand = false)
    return editingList
}