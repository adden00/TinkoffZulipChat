package com.addenisov00.courseproject.presentation.main_screen.channels.models

import com.addenisov00.courseproject.common.delegate_utills.DelegateItem

 class ShimmerItem : DelegateItem {
     private val id: Int get() = hashCode()

     override fun content(): Any = this

     override fun id() = id

     override fun compareToOther(other: DelegateItem) =
         (other as ShimmerItem).content() == this.content()
 }
