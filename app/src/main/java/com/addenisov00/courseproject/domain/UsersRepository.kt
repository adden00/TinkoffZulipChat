package com.addenisov00.courseproject.domain


import com.addenisov00.courseproject.presentation.main_screen.people.PersonItem

interface UsersRepository {
    suspend fun getAllUsers(): List<PersonItem>
    suspend fun getUser(userId: Int): PersonItem
}