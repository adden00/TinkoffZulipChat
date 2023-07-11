package com.addenisov00.courseproject.presentation.main_screen.channels.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.delegate_utills.AdapterDelegate
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ShimmerItem

class ChannelsShimmerAdapter : AdapterDelegate {
    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ItemHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.channel_shimmer_item, parent, false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem) = Unit

    override fun isOfViewType(item: DelegateItem): Boolean = item is ShimmerItem


}