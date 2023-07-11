package com.addenisov00.courseproject.presentation.messenger.models

data class EmojiListItem(
    val name: String,
    val code: String
) {
    fun getCodeString() = String(Character.toChars(code.toInt(16)))
}
