package com.addenisov00.courseproject.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NarrowModel(
    @SerialName("operator")
    val operator: String,
    @SerialName("operand")
    val operand: String
)