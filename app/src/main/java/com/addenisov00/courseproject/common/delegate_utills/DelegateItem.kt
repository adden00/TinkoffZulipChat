package com.addenisov00.courseproject.common.delegate_utills


interface DelegateItem {
    fun content(): Any
    fun id(): Int
    fun compareToOther(other: DelegateItem): Boolean
}
