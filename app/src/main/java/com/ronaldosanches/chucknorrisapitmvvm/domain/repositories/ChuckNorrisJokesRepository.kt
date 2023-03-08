package com.ronaldosanches.chucknorrisapitmvvm.domain.repositories

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse

interface ChuckNorrisJokesRepository {
    suspend fun getRandomJoke() : ResultChuck<JokeResponse>
    suspend fun getRandomJokeByCategory(category: String) : ResultChuck<JokeResponse>
    suspend fun getJokeBySearch(search: String) : ResultChuck<SearchResponse>
    suspend fun getCategories() : ResultChuck<List<String>>
    suspend fun saveJokeToFavorites(joke: JokeResponse) : ResultChuck<Long>
    suspend fun deleteJokeFromFavorites(joke: JokeResponse) : ResultChuck<Int>
    suspend fun checkIfJokeIsFavorited(jokeId: String) : ResultChuck<Boolean>
    fun getFavoriteJokes() : ResultChuck<List<JokeResponse>>
}