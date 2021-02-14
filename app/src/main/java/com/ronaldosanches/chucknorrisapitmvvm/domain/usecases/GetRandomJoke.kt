package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository

class GetRandomJoke(val repository: ChuckNorrisJokesRepository) {

    suspend operator fun invoke(): ResultChuck<JokeResponse> {
        return repository.getRandomJoke()
    }
}