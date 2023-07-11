package com.addenisov00.courseproject.presentation.main_screen.people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.OnClickListener
import com.addenisov00.courseproject.common.delegate_utills.AdapterDelegate
import com.addenisov00.courseproject.common.delegate_utills.DelegateItem
import com.addenisov00.courseproject.databinding.PersonItemBinding
import com.bumptech.glide.Glide

class PeopleAdapter(private val listener: OnClickListener<PersonItem>) :
    AdapterDelegate {

    inner class ItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setData(item: PersonItem) {
            val binding = PersonItemBinding.bind(view)
            binding.tvName.text = item.name
            binding.tvEmail.text = item.email
            Glide.with(binding.root.context).load(item.photo).centerCrop().into(binding.imAvatar)
            itemView.setOnClickListener {
                listener.onClick(item)
            }
            binding.statusCircle.setCardBackgroundColor(binding.root.context.getColor(item.status))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem) {
        (holder as ItemHolder).setData(item as PersonItem)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is PersonItem
}