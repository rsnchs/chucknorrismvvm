package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository


class AddJokeToFavorites(
    private val repository: ChuckNorrisJokesRepository,
    private val checkIfJokeIsFavorite: CheckIfJokeIsFavorite,
    ) {
    suspend operator fun invoke(joke: JokeResponse) : ResultChuck<JokeResponse> {
        repository.saveJokeToFavorites(joke)
        val checkIfFavorite = checkIfJokeIsFavorite(jokeId = joke.id)
        return if(checkIfFavorite is ResultChuck.Error) {
            ResultChuck.Error(checkIfFavorite.error)
        } else {
            val jokeCorrectState = joke.copy(isFavorite = (checkIfFavorite as ResultChuck.Success).data)
            ResultChuck.Success(jokeCorrectState)
        }
    }
}