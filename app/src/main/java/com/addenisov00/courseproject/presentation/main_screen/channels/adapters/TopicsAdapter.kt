package com.addenisov00.courseproject.presentation.main_screen.channels.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.OnClickListener
import com.addenisov00.courseproject.common.delegate_utills.AdapterDelegate
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.databinding.TopicItemBinding
import com.addenisov00.courseproject.presentation.main_screen.channels.models.TopicItem

class TopicsAdapter(private val listener: OnClickListener<TopicItem>) :
    AdapterDelegate {
    inner class ItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun setData(item: TopicItem) {
            val binding = TopicItemBinding.bind(view)
            binding.tvTopicName.text = item.name
            itemView.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.topic_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem) {
        (holder as ItemHolder).setData(item as TopicItem)
    }

    override fun isOfViewType(item: DelegateItem): Boolean {
        return item is TopicItem
    }

}