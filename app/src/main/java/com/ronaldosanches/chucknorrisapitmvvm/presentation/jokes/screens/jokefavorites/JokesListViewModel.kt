package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokefavorites

import androidx.lifecycle.*
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetAllFavoriteJokes
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.RemoveJokeFromFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokesListViewModel @Inject constructor(
    private val favoriteJokes: GetAllFavoriteJokes,
    private val removeJokeFromFavorites: RemoveJokeFromFavorites) : ViewModel() {

    private val _jokesList : MutableLiveData<ResultChuck<List<ViewType>>> = MutableLiveData(ResultChuck.Loading())
    val jokeList : LiveData<ResultChuck<List<ViewType>>> get() = _jokesList

    fun getFavoriteJokes() = viewModelScope.launch {
        val jokes = favoriteJokes()
        if(jokes is ResultChuck.Success) {
            val dbList = jokes.success()
            val title = SectionTitleItem(R.string.favorites_title)
            val list = listOf(title,*dbList.toTypedArray())
            _jokesList.postValue(ResultChuck.Success(list))
        } else {
            _jokesList.postValue(jokes)
        }
    }

    fun removeFromFavorites(joke: JokeResponse) = viewModelScope.launch {
        removeJokeFromFavorites(joke)
        getFavoriteJokes()
    }
}