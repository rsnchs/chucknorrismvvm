package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import androidx.lifecycle.LiveData
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository

class GetAllFavoriteJokes(private val repository: ChuckNorrisJokesRepository) {

    operator fun invoke() : ResultChuck<LiveData<List<JokeResponse>>> {
        return repository.getFavoriteJokes()
    }
}