package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewTypeDelegateAdapter
import com.ronaldosanches.chucknorrisapitmvvm.databinding.LoadingContentBinding

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoadingContentBinding.inflate(inflater, parent, false)
        return JokeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as JokeItemViewHolder
    }

    inner class JokeItemViewHolder(binding: LoadingContentBinding)
        : RecyclerView.ViewHolder(binding.root)
}