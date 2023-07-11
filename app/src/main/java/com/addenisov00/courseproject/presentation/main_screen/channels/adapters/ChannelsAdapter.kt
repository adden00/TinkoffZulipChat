package com.addenisov00.courseproject.presentation.main_screen.channels.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.OnClickListener
import com.addenisov00.courseproject.common.delegate_utills.AdapterDelegate
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.common.states.ChannelClickType
import com.addenisov00.courseproject.databinding.ChannelItemBinding
import com.addenisov00.courseproject.presentation.main_screen.channels.models.ChannelItem

class ChannelsAdapter(private val listener: OnClickListener<ChannelClickType>) :
    AdapterDelegate {
    inner class ItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun setData(item: ChannelItem) {
            val binding = ChannelItemBinding.bind(view)
            binding.tvChannelName.text = item.name
            if (item.isExpand)
                binding.imExpand.setImageResource(R.drawable.channel_sellected_arrow)
            else
                binding.imExpand.setImageResource(R.drawable.channel_not_selected_arrow)
            itemView.setOnClickListener {
                listener.onClick(ChannelClickType.ChannelItemClicked(item))
            }
            binding.btnExpand.setOnClickListener {
                listener.onClick(ChannelClickType.ExpandArrowClicked(item))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ItemHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.channel_item, parent, false
        )
    )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem) {
        (holder as ItemHolder).setData(item as ChannelItem)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is ChannelItem
}