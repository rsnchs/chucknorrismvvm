package com.ronaldosanches.chucknorrisapitmvvm.core.di

import com.ronaldosanches.chucknorrisapitmvvm.core.di.appcomponent.AppModule
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiLocalDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.LocalDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
class AppModuleMocked {

    @Singleton
    @Provides
    fun providesNetworkInfo() : NetworkInfo = mock(NetworkInfo::class.java)

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = mock(Retrofit::class.java)

    @Singleton
    @Provides
    fun providesRoomDb() : LocalDataBase = mock(LocalDataBase::class.java)

    @Singleton
    @Provides
    fun providesDao() : ChuckNorrisApiLocalDataSource = mock(ChuckNorrisApiLocalDataSource::class.java)
}