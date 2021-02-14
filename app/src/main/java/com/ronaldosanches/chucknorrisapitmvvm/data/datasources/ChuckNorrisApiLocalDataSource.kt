package com.ronaldosanches.chucknorrisapitmvvm.data.datasources

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.data.models.LastVisualizedItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse

/**
 * Store Jokes on SQLite
 */

@Dao
interface ChuckNorrisApiLocalDataSource {
    @Query("SELECT * FROM ${Constants.Sqlite.JOKES_TABLE}")
    fun getFavoriteJokes() : LiveData<List<JokeResponse>>

    @Query("SELECT * FROM ${Constants.Sqlite.JOKES_TABLE} WHERE id = :id")
    suspend fun getJokeById(id: String) : JokeResponse?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJokeToFavorites(joke: JokeResponse) : Long

    @Delete
    suspend fun deleteJokeFromFavorites(joke: JokeResponse) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJokeToLastVisualized(joke: LastVisualizedItem) : Long

    @Query("SELECT * FROM ${Constants.Sqlite.LAST_VISUALIZED_TABLE} LIMIT 1")
    suspend fun getLastVisualizedJoke() : LastVisualizedItem?

    @Query("SELECT EXISTS(SELECT * FROM ${Constants.Sqlite.JOKES_TABLE} WHERE id =:id)")
    suspend fun checkIfJokeIsFavorited(id: String) : Boolean
}