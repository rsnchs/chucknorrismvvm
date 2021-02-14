package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.GenericListItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse

class ViewTypeDiffCallback(private val oldList : List<ViewType>, private val newList : List<ViewType>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if(oldItem::class == newItem::class) {
            when(oldItem) {
                is JokeResponse -> oldItem.checkJokeResponseContent(newItem as JokeResponse)
                is GenericListItem -> oldItem.checkGenericListItemContent(newItem as GenericListItem)
                else -> false
            }
        } else {
            false
        }
    }

    private fun JokeResponse.checkJokeResponseContent(other: JokeResponse) : Boolean {
        return this.id == other.id
    }

    private fun GenericListItem.checkGenericListItemContent(other: GenericListItem) : Boolean {
        return if(this.itemTitleRes != null && other.itemTitleRes !=null) {
            this.itemTitleRes == other.itemTitleRes
        } else if (this.itemTitle != null && other.itemTitle != null) {
            this.itemTitle == other.itemTitle
        } else {
            false
        }
    }
}