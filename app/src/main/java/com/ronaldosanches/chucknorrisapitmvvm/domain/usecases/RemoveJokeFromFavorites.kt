package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository

class RemoveJokeFromFavorites(
    private val repository: ChuckNorrisJokesRepository,
    private val checkIfJokeIsFavorite: CheckIfJokeIsFavorite,
) {
    suspend operator fun invoke(joke: JokeResponse) : ResultChuck<JokeResponse> {
        repository.deleteJokeFromFavorites(joke)
        val checkIfJokeIsFavorite = checkIfJokeIsFavorite(jokeId = joke.id)
        return if(checkIfJokeIsFavorite is ResultChuck.Error) {
            ResultChuck.Error(checkIfJokeIsFavorite.error)
        } else {
            val jokeCorrectState = joke.copy(isFavorite = (checkIfJokeIsFavorite as ResultChuck.Success).data)
            ResultChuck.Success(jokeCorrectState)
        }
    }
}