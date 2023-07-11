package com.addenisov00.courseproject.data.network

object EditLateMessageException : Exception()
data class EditMessageException(val errorMessage: String) : Exception(errorMessage)
data class MessageException(val errorMessage: String) : Exception(errorMessage)