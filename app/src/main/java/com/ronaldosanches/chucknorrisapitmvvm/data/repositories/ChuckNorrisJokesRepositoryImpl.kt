package com.ronaldosanches.chucknorrisapitmvvm.data.repositories

import androidx.lifecycle.LiveData
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.CacheException
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.UserIsOfflineException
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.handleNetworkError
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiLocalDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiRemoteDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.models.LastVisualizedItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository

class ChuckNorrisJokesRepositoryImpl(
        private val networkInfo: NetworkInfo,
        private val localDataSource: ChuckNorrisApiLocalDataSource,
        private val remoteDataSource: ChuckNorrisApiRemoteDataSource) : ChuckNorrisJokesRepository {

    override suspend fun getRandomJoke(): ResultChuck<JokeResponse> {
        return handleApiCall(onlineCall = {
            val randomJoke = remoteDataSource.getRandomJoke()
            val id = randomJoke.id
            val favorite = localDataSource.checkIfJokeIsFavorited(id)
            randomJoke.isFavorite = favorite
            localDataSource.saveJokeToLastVisualized(LastVisualizedItem(joke = randomJoke))
            ResultChuck.Success(data = randomJoke)
        },
                offlineCall = {
                    val cacheJoke = localDataSource.getLastVisualizedJoke()
                    if(cacheJoke != null) {
                        ResultChuck.Success(data = cacheJoke.joke)
                    } else {
                        throw CacheException()
                    }
                })
    }

    override suspend fun getRandomJokeByCategory(category: String): ResultChuck<JokeResponse> {
        return handleApiCall(onlineCall = {
            val randomJokeByCategory = remoteDataSource.getRandomJokeByCategory(category)
            randomJokeByCategory.isFavorite = localDataSource.checkIfJokeIsFavorited(randomJokeByCategory.id)
            localDataSource.saveJokeToLastVisualized(LastVisualizedItem(joke = randomJokeByCategory))
            ResultChuck.Success(data = randomJokeByCategory)
        },
                offlineCall = {
                    val cacheJoke = localDataSource.getLastVisualizedJoke()
                    if(cacheJoke != null) {
                        ResultChuck.Success(data = cacheJoke.joke)
                    } else {
                        throw CacheException()
                    }
                })
    }

    override suspend fun getJokeBySearch(search: String): ResultChuck<SearchResponse> {
        return handleApiCall(
                onlineCall = {
                    val searchResponse = remoteDataSource.getJokeBySearch(search)
                    searchResponse.result?.forEach {
                        it.isFavorite = localDataSource.checkIfJokeIsFavorited(it.id)
                    }
                    ResultChuck.Success(data = searchResponse)
                },
                offlineCall = {
                    /**
                     * shows user is offline warning
                     */
                    throw UserIsOfflineException()
                }
        )
    }

    override suspend fun getCategories(): ResultChuck<List<String>> {
        return handleApiCall(
                onlineCall = {
                    val list = mutableListOf(Constants.CustomAttributes.CATEGORY_ALL)
                    list.addAll(remoteDataSource.getCategories().categories ?: listOf())
                    ResultChuck.Success(data = list)
                }, offlineCall = {
            /**
             * shows the All Category and nothing else
             */
            throw UserIsOfflineException()
        })
    }

    override suspend fun saveJokeToFavorites(joke: JokeResponse) : ResultChuck<Long> {
        return handleDbCall {
            joke.isFavorite = true
            ResultChuck.Success(localDataSource.saveJokeToFavorites(joke))
        }
    }

    override suspend fun deleteJokeFromFavorites(joke: JokeResponse) : ResultChuck<Int> {
        return handleDbCall {
            ResultChuck.Success(localDataSource.deleteJokeFromFavorites(joke))
        }
    }

    override suspend fun checkIfJokeIsFavorited(jokeId: String): ResultChuck<Boolean> {
        return handleDbCall {
            ResultChuck.Success(localDataSource.checkIfJokeIsFavorited(jokeId))
        }
    }

    override fun getFavoriteJokes(): ResultChuck<LiveData<List<JokeResponse>>> {
        return handleDbCall {
            ResultChuck.Success(localDataSource.getFavoriteJokes())
        }
    }

    private inline fun <T> handleApiCall(
            onlineCall: () -> ResultChuck<T>,
            offlineCall: () -> ResultChuck<T>,
    ) : ResultChuck<T> {
        return try {
            if(networkInfo.isConnected()) {
                onlineCall()
            } else {
                offlineCall()
            }
        } catch (throwable: Throwable) {
            throwable.handleNetworkError()
        }
    }

    private inline fun <T> handleDbCall(
            dbCall : ()  -> ResultChuck<T>
    ) : ResultChuck<T> {
        return try {
            dbCall()
        } catch (throwable: Throwable) {
            throwable.handleNetworkError()
        }
    }
}