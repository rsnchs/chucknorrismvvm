package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewTypeDelegateAdapter
import com.ronaldosanches.chucknorrisapitmvvm.data.models.GenericListItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import javax.inject.Inject

class DynamicListAdapter @Inject constructor(val callback: ContextRetriever)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var listItems: ArrayList<ViewType> = arrayListOf()
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.apply {
            put(Constants.ViewType.GENERIC_ITEM_LIST,ListItemDelegateAdapter(callback as? IPositionClick))
            put(Constants.ViewType.SECTION_TITLE,SectionTitleDelegateAdapter())
            put(Constants.ViewType.JOKES,JokeItemDelegateAdapter(callback as? IJokeClick))
            put(Constants.ViewType.WARNING,WarningImageDelegateAdapter())
            put(Constants.ViewType.LOADING,LoadingDelegateAdapter())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType)?.onCreateViewHolder(parent)
                ?: run {
                    throw IllegalStateException("Unknown ViewType")
                }
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return delegateAdapters.get(getItemViewType(position))
                ?.onBindViewHolder(holder, this.listItems[position], position)
                ?: run {
                    throw IllegalStateException("Unknown ViewType")
                }
    }

    override fun getItemViewType(position: Int): Int {
        return this.listItems[position].getViewType()
    }

    fun addResList(list: List<Int>) {
        listItems.clear()
        listItems.addAll(list.map { GenericListItem(it,null) })
    }

    fun updateList(list: List<ViewType>) {
        val diffCallback = ViewTypeDiffCallback(this.listItems,list)
        val diffResult : DiffUtil.DiffResult = DiffUtil
                .calculateDiff(diffCallback)
        listItems.clear()
        listItems.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateForFavoriteStateChanged(isFavorite: Boolean, itemId: String, listPosition: Int) {
        val itemPosition = findItemInJokeList(listPosition,itemId)
        if(itemPosition != Constants.Position.INVALID_POSITION) {
            (listItems[itemPosition] as JokeResponse).isFavorite = isFavorite
            notifyItemChanged(itemPosition)
        }
    }

    private fun findItemInJokeList(probablePosition: Int, itemId: String) : Int {
        return if(listItems.size >= probablePosition) {
            val itemInPosition = listItems[probablePosition]
            if(itemInPosition is JokeResponse && itemInPosition.id == itemId) {
                probablePosition
            } else {
                listItems.indexOfFirst { it is JokeResponse && it.id == itemId}
            }
        } else {
            Constants.Position.INVALID_POSITION
        }
    }
}