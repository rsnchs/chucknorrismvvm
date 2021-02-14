package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository

class GetJokeBySearch(val repository: ChuckNorrisJokesRepository) {

    suspend operator fun invoke(search: String): ResultChuck<SearchResponse> {
        return repository.getJokeBySearch(search)
    }
}