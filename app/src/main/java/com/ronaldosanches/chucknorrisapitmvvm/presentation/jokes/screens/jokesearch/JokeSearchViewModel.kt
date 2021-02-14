package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokesearch

import androidx.lifecycle.*
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.AddJokeToFavorites
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetJokeBySearch
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.RemoveJokeFromFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class JokeSearchViewModel @Inject constructor(
    val getJokeBySearch: GetJokeBySearch,
    val addJokesToFavorites: AddJokeToFavorites,
    val removeJokeFromFavorites: RemoveJokeFromFavorites,
) : ViewModel() {

    private val _jokesList = MutableLiveData<List<ViewType>>()
    val jokeList : LiveData<List<ViewType>>
        get() = _jokesList

    private var coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getJokeBySearch(query: String) = liveData<ResultChuck<SearchResponse>>(coroutineContext) {
        emit(ResultChuck.Loading(null))
        val search = getJokeBySearch(search = query)
        emit(search)
    }

    fun handleSearchResponse(searchResponse: ResultChuck<SearchResponse>?) {
        when(searchResponse) {
            is ResultChuck.Success -> showSearchResult(searchResponse.data)
            is ResultChuck.Error -> Unit
            is ResultChuck.Loading -> showloading()
        }
    }

    private fun showloading() {
        val loadingItem = object : ViewType {
            override fun getViewType() = Constants.ViewType.LOADING
        }
        _jokesList.postValue(listOf(loadingItem))
    }

    private fun showSearchResult(validResult: SearchResponse) {
        val list = validResult.result
        if(list.isNullOrEmpty()) {
            showEmptyResultList()
        } else {
            addJokesList(list)
        }
    }

    private fun addJokesList(list: List<JokeResponse>) {
        val viewTypeList = mutableListOf<ViewType>()
        viewTypeList.add(SectionTitleItem(R.string.search_title))
        viewTypeList.addAll(list)
        _jokesList.postValue(viewTypeList)
    }

    private fun showEmptyResultList() {
        val list = mutableListOf<ViewType>(WarningItem(R.string.no_results_returned, null))
        _jokesList.postValue(list)
    }

    fun handleFavoriteButtonClick(joke: JokeResponse) = liveData<ResultChuck<out Number>> {
        emit(ResultChuck.Loading(null))
        if(joke.isFavorite) {
            val amountOfItemsRemoved = removeJokeFromFavorites(joke)
            emit(amountOfItemsRemoved)
        } else {
            val idAddedJokeId = addJokesToFavorites(joke)
            emit(idAddedJokeId)
        }
    }
}