package com.addenisov00.courseproject.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "reactions")
data class ReactionEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val reaction: String,

    @ColumnInfo(name = "count")
    val count: Int,

    @ColumnInfo(name = "is_selected")
    val isSelected: Boolean,

    @ColumnInfo(name = "reaction_name")
    val reactionName: String
)