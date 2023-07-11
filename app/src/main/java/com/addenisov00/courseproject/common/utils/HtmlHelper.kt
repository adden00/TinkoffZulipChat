package com.addenisov00.courseproject.common.utils

import android.text.Html
import com.addenisov00.courseproject.common.utils.ImageHelper.containsPhoto
import com.addenisov00.courseproject.common.utils.ImageHelper.getPhotoUrl
import com.addenisov00.courseproject.presentation.messenger.models.MessageContent

interface HtmlHelper {
    fun fromHtml(content: String): MessageContent
}

class HtmlHelperImpl : HtmlHelper {
    override fun fromHtml(content: String): MessageContent {
        return if (content.containsPhoto()) {
            val url = content.getPhotoUrl()
            MessageContent("", url)
        } else
            MessageContent(
                Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT).trim()
                    .toString(), ""
            )
    }
}