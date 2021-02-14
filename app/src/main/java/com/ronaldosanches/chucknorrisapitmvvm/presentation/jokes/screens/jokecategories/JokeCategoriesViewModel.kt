package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokecategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.GenericListItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JokeCategoriesViewModel @Inject constructor(): ViewModel() {

    fun createCategoriesMenu(listCategories: Array<String>) = liveData {
        val list = mutableListOf<ViewType>(SectionTitleItem(R.string.categories_title))
        listCategories.forEach { list.add(GenericListItem(null,it)) }
        emit(Collections.unmodifiableList(list))
    }
}