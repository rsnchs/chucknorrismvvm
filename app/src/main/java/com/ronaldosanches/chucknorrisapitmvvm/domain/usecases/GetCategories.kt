package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository

class GetCategories(val repository: ChuckNorrisJokesRepository) {

    suspend operator fun invoke(): ResultChuck<List<String>> {
        return repository.getCategories()
    }
}