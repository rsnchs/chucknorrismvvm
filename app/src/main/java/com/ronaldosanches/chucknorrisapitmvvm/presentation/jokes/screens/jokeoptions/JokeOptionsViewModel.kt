package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokeoptions

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ChuckSearch
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeOptionsMenu
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.CheckIfJokeIsFavorite
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetCategories
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetJokeOptionsMenu
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetRandomJoke
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetRandomJokeByCategory
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.ToggleJokeToFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeOptionsViewModel @Inject constructor(
    val randomJoke: GetRandomJoke,
    val randomJokeByCategory: GetRandomJokeByCategory,
    val getCategories: GetCategories,
    val toggleJokeToFavorites: ToggleJokeToFavorites,
    val jokeIsFavorited: CheckIfJokeIsFavorite,
    val getJokeOptionsMenu: GetJokeOptionsMenu,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_LAST_JOKE = "last_visualized_joke_key"
        private const val KEY_LAST_CATEGORY_PICKED = "last_category_picked_key"
        private const val KEY_SELECTED_CATEGORY = "selected_category_result_key"
        private const val KEY_SELECTED_CATEGORY_STRING = "selected_category_string_value_key"
        private val INTERVAL_INVALID_SEARCH_LENGTH = 0..2
    }

//    var jokeState by mutableStateOf<ResultChuck<JokeResponse>>(ResultChuck.Loading())
//        private set

    private val _jokeState : MutableLiveData<ResultChuck<JokeResponse>> = MutableLiveData(ResultChuck.Loading())
    val jokeState : LiveData<ResultChuck<JokeResponse>> get() = _jokeState

    private val _jokeOptionsMenu : MutableLiveData<List<JokeOptionsMenu>?> = MutableLiveData(null)
    val jokeOptionsMenu : LiveData<List<JokeOptionsMenu>?> get() = _jokeOptionsMenu

    private val _categoriesResponse : MutableLiveData<ResultChuck<List<String>>?> = MutableLiveData(null)
    val categoriesResponse : LiveData<ResultChuck<List<String>>?> get() = _categoriesResponse

    private val _searchState : MutableLiveData<ChuckSearch> = MutableLiveData(ChuckSearch.Valid(String()))
    val searchState : LiveData<ChuckSearch> get() = _searchState

    init {
        updateCategory(Constants.CustomAttributes.CATEGORY_ALL)
    }

    var coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getRandomJokeByCategory(useStateSaved: Boolean) = viewModelScope.launch {
        val lastSavedJoke = savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)
        if(useStateSaved && lastSavedJoke != null) {
            _jokeState.postValue(ResultChuck.Success(lastSavedJoke))
        } else {
            _jokeState.postValue(ResultChuck.Loading())
            when(savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)) {
                Constants.CustomAttributes.CATEGORY_ALL -> {
                    val randomJoke = randomJoke()
                    _jokeState.postValue(randomJoke)
                }
                else ->{
                    val category = savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)
                    val randomJoke = randomJokeByCategory(category = category as String)
                    _jokeState.postValue(randomJoke)
                }
            }
        }
    }

    fun getCategoriesFromApi() = viewModelScope.launch {
        _categoriesResponse.postValue(ResultChuck.Loading())
        _jokeState.postValue(ResultChuck.Loading())
        val categories = getCategories()
        _categoriesResponse.postValue(categories)
    }

    fun checkIfJokeIsFavorite() = viewModelScope.launch {
        val lastSavedJoke = savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)
        if(lastSavedJoke != null) {
            val isFavorite = jokeIsFavorited(lastSavedJoke.id)
            if(isFavorite is ResultChuck.Success) {
                _jokeState.postValue(
                    ResultChuck.Success(lastSavedJoke.copy(isFavorite = isFavorite.success()))
                )
            }
        }
    }

    fun handleFavoriteButtonClick() = viewModelScope.launch {
        println("estado anterior: ${_jokeState.value}")
        val lastSavedJoke = savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)
        if(lastSavedJoke != null) {
            val jokeResponse = toggleJokeToFavorites(lastSavedJoke)
            if(jokeResponse is ResultChuck.Error) {
                _jokeState.postValue(ResultChuck.Error(jokeResponse.error))
            } else {
                _jokeState.postValue(jokeResponse)
                println("novo estado: ${_jokeState.value}")
            }
        } else {
            _jokeState.postValue(ResultChuck.Error(ErrorEntity.NotFound))
        }
    }

    fun createMenuOptions() = viewModelScope.launch {
        val menuItems = getJokeOptionsMenu()
        _jokeOptionsMenu.postValue(menuItems)
    }

    fun isSearchQueryValid(text: String) {
        if(text.length in INTERVAL_INVALID_SEARCH_LENGTH) {
            _searchState.postValue(ChuckSearch.Invalid(text))
        } else {
            _searchState.postValue(ChuckSearch.Valid(text))
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

    fun updateCategory(category: String) {
        savedStateHandle[KEY_LAST_CATEGORY_PICKED] = category
    }

    fun updateLastJoke(joke: JokeResponse) {
        savedStateHandle[KEY_LAST_JOKE] = joke
    }

    fun resetCategories() {
        _categoriesResponse.postValue(null)
    }
}