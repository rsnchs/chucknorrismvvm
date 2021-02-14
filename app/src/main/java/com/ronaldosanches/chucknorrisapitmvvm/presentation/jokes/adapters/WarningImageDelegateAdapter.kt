package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewTypeDelegateAdapter
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.databinding.WarningContentBinding

class WarningImageDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WarningContentBinding.inflate(inflater, parent, false)
        return JokeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as JokeItemViewHolder
        holder.bind(item as WarningItem)
    }

    inner class JokeItemViewHolder(private val binding: WarningContentBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: WarningItem) {
            if(listItem.title != null) {
                binding.tvWarningTitle.text = binding.root.context.getText(listItem.title)
            }
            if(listItem.drawable != null) {
                binding.ivWarningImage.setImageResource(listItem.drawable)
            }
        }
    }
}