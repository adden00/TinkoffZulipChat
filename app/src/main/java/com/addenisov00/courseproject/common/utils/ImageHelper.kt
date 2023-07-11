package com.addenisov00.courseproject.common.utils

import com.addenisov00.courseproject.common.Constants

object ImageHelper {

    fun String.getPhotoUrl(): String {
        return try {
            val url = this.split("href=\"/user_uploads/")[1].split("\"")[0]
            this.replace(url, "")
            Constants.IMAGE_URL + "/user_uploads/" + url
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun String.containsPhoto(): Boolean {
        return this.contains("user_uploads")
    }
}