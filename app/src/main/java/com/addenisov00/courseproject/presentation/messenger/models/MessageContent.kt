package com.addenisov00.courseproject.presentation.messenger.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MessageContent(
    val text: String,
    val imageUrl: String
) : Parcelable
