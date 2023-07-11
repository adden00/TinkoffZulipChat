package com.addenisov00.courseproject.presentation.messenger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.addenisov00.courseproject.common.delegate_utills.AdapterDelegate
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.databinding.MessageItemBinding
import com.addenisov00.courseproject.presentation.custom_views.ReactionView
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import com.addenisov00.courseproject.presentation.utills.MessageClickListener

class MessageDelegatedAdapter(private val listener: MessageClickListener) : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemHolder(
            MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem
    ) {
        (holder as ItemHolder).setData(item.content() as MessageItem, listener)
    }

    override fun isOfViewType(item: DelegateItem): Boolean {
        return item is MessageItem
    }

    class ItemHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(message: MessageItem, listener: MessageClickListener) {
            binding.message.setMessage(message)
            itemView.setOnLongClickListener {
                listener.onMessageClick(message)
                true
            }
            binding.message.getReactionBox().forEach {
                it.setOnClickListener { reactionView ->
                    listener.onReactionClick(message, (reactionView as ReactionView).getReaction())
                }
            }
        }
    }
}