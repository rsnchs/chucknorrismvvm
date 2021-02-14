package com.ronaldosanches.chucknorrisapitmvvm.core.di.appcomponent

import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun providesRemoteDataSource(retrofit: Retrofit) : ChuckNorrisApiRemoteDataSource
            = retrofit.create(ChuckNorrisApiRemoteDataSource::class.java)
}