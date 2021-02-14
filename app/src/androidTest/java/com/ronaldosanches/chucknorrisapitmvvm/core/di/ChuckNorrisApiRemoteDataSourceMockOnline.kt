package com.ronaldosanches.chucknorrisapitmvvm.core.di

import com.ronaldosanches.chucknorrisapitmvvm.ObjectResources
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiRemoteDataSource
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import retrofit2.http.Query

class ChuckNorrisApiRemoteDataSourceMockOnline : ChuckNorrisApiRemoteDataSource, ObjectResources() {

        override suspend fun getRandomJoke() : JokeResponse {
                return mockJokeResponse
        }

        override suspend fun getRandomJokeByCategory(@Query(Constants.ApiUrls.CATEGORY_PARAM) category : String)
                : JokeResponse {
                return mockJokeResponse
        }

        override suspend fun getJokeBySearch(@Query(Constants.ApiUrls.QUERY_PARAM) search: String): SearchResponse {
                return mockSearchResponse
        }

        override suspend fun getCategories() : CategoryResponse {
                return mockCategoryResponse
        }
}