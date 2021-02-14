package com.ronaldosanches.chucknorrisapitmvvm.core.di.appcomponent

import android.content.Context
import androidx.room.Room
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.ConnectionCallback
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.DeviceNetworkSettings
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiLocalDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.LocalDataBase
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryCustomAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesNetworkInfo(@ApplicationContext context: Context,
                            deviceNetworkSettings: DeviceNetworkSettings,
                            connectionCallback: ConnectionCallback) : NetworkInfo {
        return  NetworkInfo(context, deviceNetworkSettings,connectionCallback)
    }

    @Singleton
    @Provides
    fun providesOkHttpInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides fun providesOkHttpClient(okhttpInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
                .addNetworkInterceptor(okhttpInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient)
            : Retrofit {

        val moshi = Moshi.Builder().add(CategoryCustomAdapter()).build()
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Constants.ApiUrls.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesRoomDb(@ApplicationContext context: Context) : LocalDataBase {
        return Room.databaseBuilder(context,LocalDataBase::class.java,
            Constants.Sqlite.DB_NAME).build()
    }

    @Singleton
    @Provides
    fun providesDao(roomDataBase: LocalDataBase) : ChuckNorrisApiLocalDataSource = roomDataBase.jokeDao()
}