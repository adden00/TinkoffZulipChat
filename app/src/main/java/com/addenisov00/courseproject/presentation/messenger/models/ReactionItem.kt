package com.addenisov00.courseproject.presentation.messenger.models

import android.os.Parcelable
import com.addenisov00.courseproject.presentation.custom_views.ReactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReactionItem(
    val reaction: String,
    val count: Int,
    val isSelected: Boolean,
    val reactionName: String,
    val reactionType: ReactionType
) : Parcelable {

    override fun toString(): String {
        return when (reactionType) {
            ReactionType.REACTION -> "$reaction$count"
            ReactionType.ADD_NEW_REACTION -> "+"
        }
    }
}
