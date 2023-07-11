package com.addenisov00.courseproject.presentation.messenger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.addenisov00.courseproject.common.delegate_utills.AdapterDelegate
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.databinding.DateItemBinding
import com.addenisov00.courseproject.presentation.messenger.models.DateItem

class DateDelegatedAdapter : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemHolder(
            DateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem
    ) {
        (holder as ItemHolder).setData(item as DateItem)
    }

    override fun isOfViewType(item: DelegateItem) = item is DateItem

    class ItemHolder(private val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DateItem) {
            binding.tvDate.text = item.date
        }
    }
}