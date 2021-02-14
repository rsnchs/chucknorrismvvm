package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse

interface IJokeClick : ContextRetriever {
    fun favoriteClick(joke: JokeResponse, position: Int)
    fun shareClick(joke: JokeResponse)
}