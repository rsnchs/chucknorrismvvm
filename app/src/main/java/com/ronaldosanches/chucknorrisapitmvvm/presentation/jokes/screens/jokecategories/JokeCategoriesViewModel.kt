package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokecategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.CreateCategoriesMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeCategoriesViewModel @Inject constructor(
    val categories: CreateCategoriesMenu,
    ): ViewModel() {

    private val _categoriesResponse : MutableLiveData<List<ViewType>> = MutableLiveData(listOf(SectionTitleItem(R.string.categories_title)))
    val categoriesResponse : LiveData<List<ViewType>> get() = _categoriesResponse

    fun createCategoriesMenu(listCategories: Array<String>) = viewModelScope.launch {
        val categoriesMenu = categories(listCategories)
        _categoriesResponse.postValue(categoriesMenu)
    }
}