package com.addenisov00.courseproject.data.repository

import com.addenisov00.courseproject.data.network.MessageApiClient
import com.addenisov00.courseproject.data.network.models.Aggregated
import com.addenisov00.courseproject.data.network.models.Status
import com.addenisov00.courseproject.data.network.models.toPersonItem
import com.addenisov00.courseproject.domain.UsersRepository
import com.addenisov00.courseproject.presentation.main_screen.people.PersonItem

class UsersRepositoryImpl(private val api: MessageApiClient) : UsersRepository {

    override suspend fun getAllUsers(): List<PersonItem> {
        val peopleList = api.getAllUsers().members
        val peopleStatusList = api.getAllUserStatuses()

        val result = peopleList.map {
            val status = peopleStatusList.state[it.email]
            it.toPersonItem(status ?: nullStatus)
        }.sortedByDescending {
            it.timeStamp
        }
        return result
    }


    override suspend fun getUser(userId: Int): PersonItem {
        val user = api.getUser(userId).user
        val userStatus = api.getUserStatus(userId)
        return user.toPersonItem(status = userStatus.status)
    }

    companion object {
        private val nullStatus = Status(Aggregated("OFFLINE", 0))
    }

}