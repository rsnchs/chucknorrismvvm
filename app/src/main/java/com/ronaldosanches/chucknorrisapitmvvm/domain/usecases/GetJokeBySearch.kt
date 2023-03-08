package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.ErrorItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetJokeBySearch(val repository: ChuckNorrisJokesRepository) {
    suspend operator fun invoke(query: String): ResultChuck<List<ViewType>> = withContext(Dispatchers.IO) {
        val search = repository.getJokeBySearch(query)
        val list : MutableList<ViewType> = mutableListOf()
        if(search is ResultChuck.Success) {
            if(search.success().result.isNullOrEmpty()) {
                list.add(WarningItem(R.string.no_results_returned, null))
            } else {
                list.addAll(search.success().result!!)
            }
        } else {
            list.add(ErrorItem((search as ResultChuck.Error).error))
        }
        return@withContext ResultChuck.Success(list)
    }
}