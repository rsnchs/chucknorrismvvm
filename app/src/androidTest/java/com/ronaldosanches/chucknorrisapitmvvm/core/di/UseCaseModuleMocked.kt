package com.ronaldosanches.chucknorrisapitmvvm.core.di

import androidx.lifecycle.SavedStateHandle
import com.ronaldosanches.chucknorrisapitmvvm.core.di.activitycontext.UseCaseModule
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [UseCaseModule::class])
class UseCaseModuleMocked {

    @Singleton
    @Provides
    fun providesAddJokeToFavorites() : AddJokeToFavorites {
        return mock(AddJokeToFavorites::class.java)
    }

    @Singleton
    @Provides
    fun providesCheckIfJokeIsFavorited() : CheckIfJokeIsFavorited {
        return mock(CheckIfJokeIsFavorited::class.java)
    }

    @Singleton
    @Provides
    fun providesGetAllFavoriteJokes() : GetAllFavoriteJokes {
        return mock(GetAllFavoriteJokes::class.java)
    }

    @Singleton
    @Provides
    fun providesGetAllCategories() : GetCategories {
        return mock(GetCategories::class.java)
    }

    @Singleton
    @Provides
    fun providesGetJokeBySearch() : GetJokeBySearch {
        return mock(GetJokeBySearch::class.java)
    }

    @Singleton
    @Provides
    fun providesGetRandomJoke() : GetRandomJoke {
        return mock(GetRandomJoke::class.java)
    }

    @Singleton
    @Provides
    fun providesGetRandomJokeByCategory() : GetRandomJokeByCategory {
        return mock(GetRandomJokeByCategory::class.java)
    }

    @Singleton
    @Provides
    fun providesRemoveJokesFromFavorites() : RemoveJokeFromFavorites {
        return mock(RemoveJokeFromFavorites::class.java)
    }
//
//    @Singleton
//    @Provides
//    fun providesSavedStateHandle() : SavedStateHandle {
//        return mock(SavedStateHandle::class.java)
//    }
}