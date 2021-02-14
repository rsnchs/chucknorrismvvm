package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository


class CheckIfJokeIsFavorited(private val repository: ChuckNorrisJokesRepository) {
    suspend operator fun invoke(jokeId: String) : ResultChuck<Boolean> {
        return repository.checkIfJokeIsFavorited(jokeId)
    }
}