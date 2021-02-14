package com.ronaldosanches.chucknorrisapitmvvm.core.di.activitycontext

import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiLocalDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiRemoteDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.repositories.ChuckNorrisJokesRepositoryImpl
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class JokeViewModelModule {

    @Provides
    @ActivityRetainedScoped
    fun providesJokeRepository(
        networkInfo: NetworkInfo,
        localDataSource: ChuckNorrisApiLocalDataSource,
        remoteDataSource: ChuckNorrisApiRemoteDataSource) : ChuckNorrisJokesRepository =
        ChuckNorrisJokesRepositoryImpl(networkInfo, localDataSource, remoteDataSource)
}