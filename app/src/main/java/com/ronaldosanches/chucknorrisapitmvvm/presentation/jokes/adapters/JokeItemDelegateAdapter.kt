package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewTypeDelegateAdapter
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.setOnSafeClickListener
import com.ronaldosanches.chucknorrisapitmvvm.databinding.JokeCardContentBinding
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse

class JokeItemDelegateAdapter(private val callback: IJokeClick?) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JokeCardContentBinding.inflate(inflater, parent, false)
        return JokeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as JokeItemViewHolder
        holder.bind(item as JokeResponse, position)
    }

    inner class JokeItemViewHolder(private val binding: JokeCardContentBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: JokeResponse, position: Int) {
            if(listItem.isLoading) {
                binding.inJokeCardJoke.btJokeContentFavorite.setOnClickListener(null)
                binding.inJokeCardJoke.slJokeContentFavorite.showShimmer(true)
                binding.inJokeCardJoke.slJokeContentFavorite.visibility = View.VISIBLE

            } else {
                binding.inJokeCardJoke.slJokeContentFavorite.hideShimmer()
                binding.inJokeCardJoke.slJokeContentFavorite.visibility = View.GONE
                binding.inJokeCardJoke.btJokeContentFavorite.setOnSafeClickListener {
                    listItem.isFavorite = binding.inJokeCardJoke.btJokeContentFavorite.isSelected
                    callback?.favoriteClick(listItem, position)
                }

            }

            binding.inJokeCardJoke.apply {
                tvJokeContentJoke.text = listItem.value
                btJokeContentFavorite.isSelected = listItem.isFavorite
                btJokeContentShare.setOnSafeClickListener {
                    callback?.shareClick(listItem)
                }
            }
        }
    }
}