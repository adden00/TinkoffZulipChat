package com.addenisov00.courseproject.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "status")
    val status: Int,

    @ColumnInfo(name = "photo")
    val photo: String,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long
)

