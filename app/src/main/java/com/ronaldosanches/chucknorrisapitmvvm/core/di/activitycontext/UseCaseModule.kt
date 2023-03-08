package com.ronaldosanches.chucknorrisapitmvvm.core.di.activitycontext

import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class UseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun providesJokeOptionsMenu() : GetJokeOptionsMenu {
        return GetJokeOptionsMenu()
    }
    @Provides
    @ActivityRetainedScoped
    fun providesAddJokeToFavorites(repository: ChuckNorrisJokesRepository, checkIfJokeIsFavorite: CheckIfJokeIsFavorite) : AddJokeToFavorites {
        return AddJokeToFavorites(repository, checkIfJokeIsFavorite)
    }


    @Provides
    @ActivityRetainedScoped
    fun providesCheckIfJokeIsFavorited(repository: ChuckNorrisJokesRepository) : CheckIfJokeIsFavorite {
        return CheckIfJokeIsFavorite(repository)
    }

    @Provides
    @ActivityRetainedScoped
    fun providesGetAllFavoriteJokes(repository: ChuckNorrisJokesRepository) : GetAllFavoriteJokes {
        return GetAllFavoriteJokes(repository)
    }

    @Provides
    @ActivityRetainedScoped
    fun providesGetAllCategories(repository: ChuckNorrisJokesRepository) : GetCategories {
        return GetCategories(repository)
    }

    @Provides
    @ActivityRetainedScoped
    fun providesGetJokeBySearch(repository: ChuckNorrisJokesRepository) : GetJokeBySearch {
        return GetJokeBySearch(repository)
    }

    @Provides
    @ActivityRetainedScoped
    fun providesGetRandomJoke(repository: ChuckNorrisJokesRepository) : GetRandomJoke {
        return GetRandomJoke(repository)
    }

    @Provides
    @ActivityRetainedScoped
    fun providesGetRandomJokeByCategory(repository: ChuckNorrisJokesRepository) : GetRandomJokeByCategory {
        return GetRandomJokeByCategory(repository)
    }

    @Provides
    @ActivityRetainedScoped
    fun providesRemoveJokesFromFavorites(repository: ChuckNorrisJokesRepository, checkIfJokeIsFavorite: CheckIfJokeIsFavorite) : RemoveJokeFromFavorites {
        return RemoveJokeFromFavorites(repository, checkIfJokeIsFavorite)
    }

    @Provides
    @ActivityRetainedScoped
    fun providesToggleToFavorites(addJokeToFavorites: AddJokeToFavorites, removeJokeFromFavorites: RemoveJokeFromFavorites) : ToggleJokeToFavorites {
        return ToggleJokeToFavorites(addJokeToFavorites, removeJokeFromFavorites)
    }

    @Provides
    @ActivityRetainedScoped
    fun providesCategories() = CreateCategoriesMenu()
}