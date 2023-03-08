package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse

class ToggleJokeToFavorites(
    private val addJokeToFavorites: AddJokeToFavorites,
    private val removeFromFavorites: RemoveJokeFromFavorites,
) {
    suspend operator fun invoke(joke: JokeResponse) : ResultChuck<JokeResponse> {
        return if(joke.isFavorite) {
            removeFromFavorites(joke)
        } else {
            addJokeToFavorites(joke)
        }
    }
}