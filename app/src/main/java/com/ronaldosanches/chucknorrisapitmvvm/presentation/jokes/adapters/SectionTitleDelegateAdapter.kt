package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewTypeDelegateAdapter
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.databinding.SectionTitleItemBinding

class SectionTitleDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SectionTitleItemBinding.inflate(inflater, parent, false)
        return SectionTitleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as SectionTitleViewHolder
        holder.bind(item as SectionTitleItem)
    }

    inner class SectionTitleViewHolder(private val binding: SectionTitleItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: SectionTitleItem) {
            binding.tvSectionTitleTitle.text = binding.root.context.getString(listItem.stringId)
        }
    }
}