package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokefavorites

import androidx.lifecycle.*
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetAllFavoriteJokes
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.RemoveJokeFromFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JokesListViewModel @Inject constructor(
    private val favoriteJokes: GetAllFavoriteJokes,
    private val removeJokeFromFavorites: RemoveJokeFromFavorites) : ViewModel() {

    private val _jokesList = MutableLiveData<List<ViewType>>()
    val jokeList : LiveData<List<ViewType>>
    get() = _jokesList

    fun showAllFavoriteJokes() = favoriteJokes.invoke()

    fun removeFromFavories(joke: JokeResponse) = liveData {
        val response = removeJokeFromFavorites(joke)
        emit(response)
    }

    fun formatJokesList(favoritesList: List<JokeResponse>) {
        val list = mutableListOf<ViewType>()
        if(favoritesList.isNotEmpty()) {
            list.add(SectionTitleItem(R.string.favorites_title))
            list.addAll(favoritesList)
        } else {
         list.add(WarningItem(null,null))
        }
        _jokesList.postValue(Collections.unmodifiableList(list))
    }
}