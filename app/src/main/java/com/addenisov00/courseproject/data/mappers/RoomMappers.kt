package com.addenisov00.courseproject.data.mappers

import com.addenisov00.courseproject.common.states.InfoForMessenger
import com.addenisov00.courseproject.data.local.entities.AllChannelEntity
import com.addenisov00.courseproject.data.local.entities.MessageEntity
import com.addenisov00.courseproject.data.local.entities.MyChannelEntity
import com.addenisov00.courseproject.data.local.entities.ReactionEntity
import com.addenisov00.courseproject.data.local.entities.TopicEntity
import com.addenisov00.courseproject.presentation.custom_views.ReactionType
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import com.addenisov00.courseproject.presentation.messenger.models.ReactionItem

fun ChannelItem.toMyChannelEntity() =
    MyChannelEntity(id = id, name = name, isExpand = isExpand)

fun ChannelItem.toAllChannelEntity() =
    AllChannelEntity(id = id, name = name, isExpand = isExpand)

fun MyChannelEntity.toChannelItem() =
    ChannelItem(id = id, name = name, isExpand = isExpand)

fun AllChannelEntity.toChannelItem() =
    ChannelItem(id = id, name = name, isExpand = isExpand)

fun TopicEntity.toTopicItem() =
    TopicItem(name = name, channelId = channelId, channelName = channelName)

fun TopicItem.toEntity() =
    TopicEntity(
        name = name,
        channelId = channelId,
        uniqueName = uniqueName,
        channelName = channelName
    )

fun MessageEntity.toMessageItem() =
    MessageItem(
        id = id,
        userName = userName,
        userId = userId,
        content = message,
        reactions = reactions.map { it.toReactionItem() },
        isMy = isMy,
        imageUrl = imageUrl,
        timeStamp = timeStamp
    )

fun MessageItem.toEntity(currentItem: InfoForMessenger) =
    MessageEntity(
        id = id,
        userName = userName,
        userId = userId,
        message = content,
        reactions = reactions.map { it.toEntity() },
        isMy = isMy,
        imageUrl = imageUrl,
        timeStamp = timeStamp,
        topicUniqueName = (currentItem as? InfoForMessenger.Topic)?.TopicItem?.uniqueName,
        channelID = currentItem.getChannelId()
    )

fun ReactionEntity.toReactionItem() =
    ReactionItem(
        reaction = reaction,
        count = count,
        isSelected = isSelected,
        reactionName = reactionName,
        reactionType = ReactionType.REACTION
    )

fun ReactionItem.toEntity() =
    ReactionEntity(
        reaction = reaction,
        count = count,
        isSelected = isSelected,
        reactionName = reactionName
    )
