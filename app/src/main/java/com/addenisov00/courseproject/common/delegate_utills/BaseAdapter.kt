package com.addenisov00.courseproject.common.delegate_utills

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter :
    ListAdapter<DelegateItem, RecyclerView.ViewHolder>(DelegateAdapterItemCallback()) {
    private val delegateAdapters = mutableListOf<AdapterDelegate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        delegateAdapters[viewType].onBindViewHolder(holder, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return delegateAdapters.indexOfFirst {
            it.isOfViewType(currentList[position])
        }
    }

    fun addDelegate(delegate: AdapterDelegate) {
        delegateAdapters.add(delegate)
    }
}

