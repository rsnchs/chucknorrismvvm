package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository

class GetRandomJokeByCategory(val repository: ChuckNorrisJokesRepository) {
    suspend operator fun invoke(category: String): ResultChuck<JokeResponse> {
        return repository.getRandomJokeByCategory(category)
    }
}