package com.ronaldosanches.chucknorrisapitmvvm.data.datasources

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.data.models.LastVisualizedItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.StringListConverter

@Database(entities = [JokeResponse::class, LastVisualizedItem::class],
        version = Constants.Sqlite.DB_VERSION, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class LocalDataBase : RoomDatabase() {
    abstract fun jokeDao() : ChuckNorrisApiLocalDataSource
}