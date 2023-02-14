package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokeoptions

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.AddJokeToFavorites
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.CheckIfJokeIsFavorited
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetCategories
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetRandomJoke
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetRandomJokeByCategory
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.RemoveJokeFromFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeOptionsViewModel @Inject constructor(
    val randomJoke: GetRandomJoke,
    val randomJokeByCategory: GetRandomJokeByCategory,
    val getCategories: GetCategories,
    val addJokesToFavorites: AddJokeToFavorites,
    val jokeIsFavorited: CheckIfJokeIsFavorited,
    val removeJokeFromFavorites: RemoveJokeFromFavorites,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_LAST_JOKE = "last_visualized_joke_key"
        private const val KEY_LAST_CATEGORY_PICKED = "last_category_picked_key"
        private const val KEY_SELECTED_CATEGORY = "selected_category_result_key"
        private const val KEY_SELECTED_CATEGORY_STRING = "selected_category_string_value_key"
        private val INTERVAL_INVALID_SEARCH_LENGTH = 0..2
    }

    init {
        updateCategory(Constants.CustomAttributes.CATEGORY_ALL)
    }

    var coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getRandomJokeByCategory(useStateSaved: Boolean) = liveData(coroutineContext) {
        val lastSavedJoke = savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)
        if(useStateSaved && lastSavedJoke != null) {
            emit(ResultChuck.Success(lastSavedJoke))
        } else {
            emit(ResultChuck.Loading(null))
            when(savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)) {
                Constants.CustomAttributes.CATEGORY_ALL -> {
                    val randomJoke = randomJoke()
                    emit(randomJoke)
                }
                else ->{
                    val category = savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)
                    val randomJoke = randomJokeByCategory(category = category as String)
                    emit(randomJoke)
                }
            }
        }
    }

    fun getCategoriesFromApi() =  liveData<ResultChuck<List<String>>>(coroutineContext) {
        emit(ResultChuck.Loading(null))
        val categories = getCategories()
        emit(categories)
    }

    fun checkIfJokeIsFavorited() = viewModelScope.launch {
        val lastSavedJoke = savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)
        lastSavedJoke?.id?.let {
            when(val response = jokeIsFavorited(it)) {
                is ResultChuck.Success -> updateFavoriteState(response.data)
                else -> Unit
            }
        }
    }

    fun handleFavoriteButtonClick() = liveData<ResultChuck<out Number>>(coroutineContext) {
        emit(ResultChuck.Loading(null))
        val lastSavedJoke = savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)
        if(lastSavedJoke != null) {
            if(lastSavedJoke.isFavorite) {
                val amountOfItemsRemoved = removeJokeFromFavorites(lastSavedJoke)
                emit(amountOfItemsRemoved)
            } else {
                val idAddedJokeId = addJokesToFavorites(lastSavedJoke)
                emit(idAddedJokeId)
            }
        } else {
            emit(ResultChuck.Error(ErrorEntity.NotFound))
        }
    }

    fun createMenuOptions() = liveData {
        val listItem = listOf(R.string.menu_choose_category, R.string.menu_show_all_favorites)
        emit(listItem)
    }

    fun isSearchQueryValid(textLength: Int) = liveData {
        if(textLength in INTERVAL_INVALID_SEARCH_LENGTH) {
            emit(false)
        } else {
            emit(true)
        }
    }

    fun handleCategoryResult(requestKey: String, bundle: Bundle) = liveData {
        if(requestKey == KEY_SELECTED_CATEGORY && bundle.containsKey(KEY_SELECTED_CATEGORY_STRING)) {
            emit(bundle.getString(KEY_SELECTED_CATEGORY_STRING))
        }
    }

    fun shareJoke() = liveData {
        val jokeText = savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)?.value
        if(jokeText != null) {
            emit(jokeText)
        }
    }

    fun updateUICategory(): LiveData<String> {
        return savedStateHandle.getLiveData(KEY_LAST_CATEGORY_PICKED)
    }

    fun updateUIJoke(): LiveData<JokeResponse> {
        return savedStateHandle.getLiveData(KEY_LAST_JOKE)
    }

    fun updateFavoriteState(isFavorite: Boolean) {
        val joke = savedStateHandle.get<JokeResponse?>(KEY_LAST_JOKE)
        joke?.isFavorite = isFavorite
        savedStateHandle.set(KEY_LAST_JOKE,joke)
    }

    fun updateCategory(category: String) {
        savedStateHandle[KEY_LAST_CATEGORY_PICKED] = category
    }

    fun updateLastJoke(joke: JokeResponse) {
        savedStateHandle[KEY_LAST_JOKE] = joke
    }
}