package com.addenisov00.courseproject.data.network.models

import com.addenisov00.courseproject.common.states.PeopleOnlineStatus
import com.addenisov00.courseproject.presentation.main_screen.people.PersonItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UsersResponse(
    @SerialName("members")
    val members: List<UserDto>,
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String,
)

@Serializable
data class UserResponse(
    @SerialName("user")
    val user: UserDto,
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String,
)

@Serializable
data class UserStatusResponse(
    @SerialName("msg")
    val message: String,
    @SerialName("presence")
    val status: Status,
    @SerialName("result")
    val result: String
)

@Serializable
data class UserDto(
    @SerialName("user_id")
    val id: Int,
    @SerialName("full_name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("is_active")
    val isActive: Boolean
)


@Serializable
data class AllPeopleStatusResponse(
    @SerialName("msg")
    val message: String,
    @SerialName("result")
    val result: String,
    @SerialName("presences")
    val state: Map<String, Status>
)

@Serializable
data class Status(
    @SerialName("aggregated")
    val onlineStatus: Aggregated,
)

@Serializable
data class Aggregated(
    @SerialName("status")
    val status: String,
    @SerialName("timestamp")
    val timestamp: Long
)


fun UserDto.toPersonItem(status: Status): PersonItem {
    return PersonItem(
        this.id,
        this.name,
        this.email,
        status = status.getOnlineStatus(),
        avatarUrl ?: "",
        timeStamp = status.onlineStatus.timestamp

    )
}

private fun Status.getOnlineStatus() = run {
    val currentTime = System.currentTimeMillis() / 1000 // from mills to seconds
    if ((currentTime - this.onlineStatus.timestamp) < 10)
        PeopleOnlineStatus.ONLINE.value
    else if ((currentTime - this.onlineStatus.timestamp) / 60 < 10)
        PeopleOnlineStatus.IDLE.value
    else
        PeopleOnlineStatus.OFFLINE.value
}
