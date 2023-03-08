package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.ErrorItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllFavoriteJokes(private val repository: ChuckNorrisJokesRepository) {

    suspend operator fun invoke() : ResultChuck<List<ViewType>> = withContext(Dispatchers.IO) {
        val favoriteJokes = repository.getFavoriteJokes()
        val list = mutableListOf<ViewType>()
        when(favoriteJokes) {
            is ResultChuck.Error -> {
                list.add(ErrorItem(favoriteJokes.error))
            }
            is ResultChuck.Loading -> Unit
            is ResultChuck.Success -> {
                if(favoriteJokes.data.isEmpty()) {
                    list.add(WarningItem())
                } else {
                    list.addAll(favoriteJokes.success())
                }
            }
        }
        return@withContext ResultChuck.Success(list)
    }
}