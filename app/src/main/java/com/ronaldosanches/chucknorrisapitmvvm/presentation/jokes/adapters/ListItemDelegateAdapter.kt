package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewTypeDelegateAdapter
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.setOnSafeClickListener
import com.ronaldosanches.chucknorrisapitmvvm.data.models.GenericListItem
import com.ronaldosanches.chucknorrisapitmvvm.databinding.MenuItemBinding

class ListItemDelegateAdapter(private val callback: IPositionClick?) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MenuItemBinding.inflate(inflater, parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as ListItemViewHolder
        holder.bind(item as GenericListItem, position)
    }

    inner class ListItemViewHolder(private val binding: MenuItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: GenericListItem, position: Int) {

            val text : String = if(listItem.itemTitleRes != null) {
                binding.root.context.getString(listItem.itemTitleRes)
            } else {
                listItem.itemTitle ?: String()
            }

            binding.tvMenuItemTitle.text = text

            binding.tvMenuItemTitle.setOnSafeClickListener {
                callback?.positionClick(position, text)
            }
        }
    }
}