package com.ronaldosanches.chucknorrisapitmvvm.data.datasources

import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckNorrisApiRemoteDataSource {

    @GET(Constants.ApiUrls.RANDOM_URL_MODIFIER)
    suspend fun getRandomJoke() : JokeResponse

    @GET(Constants.ApiUrls.RANDOM_URL_MODIFIER)
    suspend fun getRandomJokeByCategory(@Query(Constants.ApiUrls.CATEGORY_PARAM) category : String)
    : JokeResponse

    @GET(Constants.ApiUrls.SEARCH_URL_MODIFIER)
    suspend fun getJokeBySearch(@Query(Constants.ApiUrls.QUERY_PARAM) search: String)
    : SearchResponse

    @GET(Constants.ApiUrls.CATEGORIES_URL_MODIFIER)
    suspend fun getCategories() : CategoryResponse
}