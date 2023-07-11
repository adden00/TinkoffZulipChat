package com.addenisov00.courseproject.presentation.main_screen.people

import android.os.Parcelable
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonItem(
    val id: Int,
    val name: String,
    val email: String,
    var status: Int,
    val photo: String,
    val timeStamp: Long
) : DelegateItem, Parcelable {

    override fun content(): Any = this
    override fun id(): Int = id
    override fun compareToOther(other: DelegateItem): Boolean =
        (other as? PersonItem)?.content() == this.content()
}
