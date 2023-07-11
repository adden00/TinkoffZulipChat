package com.addenisov00.courseproject.presentation.utills

import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.presentation.messenger.models.DateItem
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

fun List<MessageItem>.addTimeStamps(): List<DelegateItem> {
    val result = mutableListOf<DelegateItem>()
    this.forEach {
        val date = DateItem(
            SimpleDateFormat(
                "dd MMM",
                Locale.getDefault()
            ).format(Date(it.timeStamp * 1000))
        )
        if (date !in result)
            result.add(date)
        result.add(it)
    }
    return result

}