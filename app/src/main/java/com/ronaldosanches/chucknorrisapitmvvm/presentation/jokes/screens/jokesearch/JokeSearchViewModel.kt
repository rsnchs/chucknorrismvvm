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
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.ToggleJokeToFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeSearchViewModel @Inject constructor(
    val searchJoke: GetJokeBySearch,
    val toggleJokeToFavorites: ToggleJokeToFavorites,
) : ViewModel() {

    private val _jokesList = MutableLiveData<ResultChuck<List<ViewType>>>()
    val jokeList : LiveData<ResultChuck<List<ViewType>>> get() = _jokesList

    private var coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getJokeBySearch(query: String) = viewModelScope.launch(coroutineContext) {
        _jokesList.postValue(ResultChuck.Loading())
        val searchResponse = searchJoke(query)
        if(searchResponse is ResultChuck.Success) {
            val title = SectionTitleItem(R.string.search_title)
            val list = listOf(title,*searchResponse.success().toTypedArray())
            _jokesList.postValue(ResultChuck.Success(list))
        } else {
            _jokesList.postValue(searchResponse)
        }
    }

    fun handleFavoriteButtonClick(joke: JokeResponse) = viewModelScope.launch(Dispatchers.IO) {
        val previousList = _jokesList.value
        val jokeWithCorrectFavoriteStatus = toggleJokeToFavorites(joke)
        if(previousList is ResultChuck.Success && jokeWithCorrectFavoriteStatus is ResultChuck.Success) {
            val newList = mutableListOf<ViewType>()
            previousList.success().forEach {
                if((it as? JokeResponse)?.id == jokeWithCorrectFavoriteStatus.success().id) {
                    newList.add(jokeWithCorrectFavoriteStatus.success())
                } else {
                    newList.add(it)
                }
            }
            _jokesList.postValue(ResultChuck.Success(newList))
        }
    }
}