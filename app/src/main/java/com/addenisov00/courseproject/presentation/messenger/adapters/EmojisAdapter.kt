package com.addenisov00.courseproject.presentation.messenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.OnClickListener
import com.addenisov00.courseproject.databinding.EmojyButtonItemBinding
import com.addenisov00.courseproject.presentation.messenger.models.EmojiListItem

class EmojisAdapter(private val listener: OnClickListener<EmojiListItem>) :
    ListAdapter<EmojiListItem, EmojisAdapter.ItemHolder>(EmojiDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.emojy_button_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position))
    }

    inner class ItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun setData(item: EmojiListItem) {
            val binding = EmojyButtonItemBinding.bind(view)
            binding.btnEmoji.text = item.getCodeString()
            binding.btnEmoji.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    object EmojiDiffUtil : DiffUtil.ItemCallback<EmojiListItem>() {

        override fun areItemsTheSame(oldItem: EmojiListItem, newItem: EmojiListItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: EmojiListItem, newItem: EmojiListItem): Boolean {
            return oldItem == newItem
        }
    }

}