package com.addenisov00.courseproject.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostResult(
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String
)