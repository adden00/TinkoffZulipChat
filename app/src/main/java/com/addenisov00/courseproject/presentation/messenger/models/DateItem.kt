package com.addenisov00.courseproject.presentation.messenger.models

import com.addenisov00.courseproject.common.delegate_utills.DelegateItem

data class DateItem(val date: String) : DelegateItem {

    private val id: Int get() = hashCode()

    override fun content(): Any = this
    override fun id() = id
    override fun compareToOther(other: DelegateItem) =
        (other as DateItem).content() == this.content()
}