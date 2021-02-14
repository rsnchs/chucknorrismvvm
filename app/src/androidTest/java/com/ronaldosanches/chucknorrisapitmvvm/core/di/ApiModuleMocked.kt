package com.ronaldosanches.chucknorrisapitmvvm.core.di

import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModuleMocked {

    @Singleton
    @Provides
    fun providesRemoteDataSourceOnline() : ChuckNorrisApiRemoteDataSource = ChuckNorrisApiRemoteDataSourceMockOnline()
}