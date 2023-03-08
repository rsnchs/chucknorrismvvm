package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.GenericListItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem

class CreateCategoriesMenu {

    suspend operator fun invoke(categories: Array<String>) : List<ViewType> {
        val list = mutableListOf<ViewType>(SectionTitleItem(R.string.categories_title))
        categories.forEach { list.add(GenericListItem(null,it)) }
        return list
    }
}