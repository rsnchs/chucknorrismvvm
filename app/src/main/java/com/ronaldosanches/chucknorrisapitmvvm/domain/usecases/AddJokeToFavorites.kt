package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository


class AddJokeToFavorites(private val repository: ChuckNorrisJokesRepository) {
    suspend operator fun invoke(joke: JokeResponse) : ResultChuck<Long> {
        return repository.saveJokeToFavorites(joke)
    }
}