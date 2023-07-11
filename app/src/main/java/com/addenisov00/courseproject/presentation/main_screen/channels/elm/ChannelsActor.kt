package com.addenisov00.courseproject.presentation.main_screen.channels.elm

import com.addenisov00.courseproject.common.states.ChannelCreateResult
import com.addenisov00.courseproject.domain.ChannelsRepository
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch

class ChannelsActor(
    private val repository: ChannelsRepository
) : Actor<ChannelsCommand, ChannelsEvent> {
    private val switcher = Switcher()
    override fun execute(command: ChannelsCommand): Flow<ChannelsEvent> = when (command) {

        is ChannelsCommand.SubscribeOnMyChannels -> {
            repository.loadMyChannelsFromCache()
                .mapEvents(
                    { model -> ChannelsEvent.Internal.MyChannelsLoadedFromCache(model) },
                    { error -> ChannelsEvent.Internal.ErrorWithDatabase(error) }
                )
        }

        is ChannelsCommand.SubscribeOnAllChannels -> {
            repository.getAllChannelsFromCache()
                .mapEvents(
                    { model -> ChannelsEvent.Internal.AllChannelsLoadedFromCache(model) },
                    { error -> ChannelsEvent.Internal.ErrorWithDatabase(error) }
                )
        }

        is ChannelsCommand.UpdateMyChannels -> {
            switcher.switch {
                flow {
                    emit(
                        repository.updateMyChannelsFromNetwork()
                    )
                }
            }.mapEvents(
                { ChannelsEvent.Internal.MyChannelsUpdated },
                { error -> ChannelsEvent.Internal.ErrorWhileChannelsLoading(error) }
            )
        }

        is ChannelsCommand.UpdateAllChannels -> {
            switcher.switch {
                flow {
                    emit(
                        repository.updateAllChannelsFromNetwork()
                    )
                }
            }
                .mapEvents(
                    { ChannelsEvent.Internal.AllChannelsUpdated },
                    { error -> ChannelsEvent.Internal.ErrorWhileChannelsLoading(error) }
                )
        }


        is ChannelsCommand.UpdateTopics -> {
            switcher.switch {
                flow<List<TopicItem>> {
                    emit(repository.getTopicsFromNetwork(command.channelItem))
                }
            }.mapEvents(
                { model ->
                    ChannelsEvent.Internal.TopicsUpdated(
                        model,
                        command.channelItem,
                        command.channelsType
                    )
                },
                { error -> ChannelsEvent.Internal.ErrorWhileTopicsLoading(error) }
            )
        }

        is ChannelsCommand.LoadTopics -> {
            switcher.switch {
                flow<List<TopicItem>> {
                    emit(repository.getTopicsFromCache(command.channelItem.id))
                }
            }.mapEvents(
                { model ->
                    ChannelsEvent.Internal.TopicsLoadedFromCache(
                        model,
                        command.channelItem,
                        command.channelsType
                    )
                },
                { error -> ChannelsEvent.Internal.ErrorWhileTopicsLoading(error) }
            )
        }

        is ChannelsCommand.CreateChannel -> {
            switcher.switch {
                flow<ChannelCreateResult> {
                    emit(repository.createChannel(command.channelName, command.channelDescription))
                }
            }.mapEvents(
                { model -> ChannelsEvent.Internal.ChannelCreated(model) },
                { error -> ChannelsEvent.Internal.ErrorWhileCreatingChannel(error) }
            )
        }
    }

}