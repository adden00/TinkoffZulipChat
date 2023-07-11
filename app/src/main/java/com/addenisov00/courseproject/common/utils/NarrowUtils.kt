package com.addenisov00.courseproject.common.utils

import com.addenisov00.courseproject.data.network.models.NarrowModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object NarrowUtils {

    fun createNarrow(channelName: String, topicName: String): String {
        return Json.encodeToString(
            serializer(),
            listOf(
                NarrowModel("stream", channelName),
                NarrowModel("topic", topicName)
            )
        )
    }

    fun createNarrow(channelName: String): String {
        return Json.encodeToString(
            serializer(),
            listOf(
                NarrowModel("stream", channelName)
            )
        )
    }
}