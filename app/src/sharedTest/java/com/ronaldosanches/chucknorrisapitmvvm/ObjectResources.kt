@file:Suppress("PackageDirectoryMismatch")

package com.ronaldosanches.chucknorrisapitmvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.convertErrorBody
import com.ronaldosanches.chucknorrisapitmvvm.data.models.LastVisualizedItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryCustomAdapter
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkStatus

@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
open class ObjectResources {
    //adapters
    val moshi: Moshi = Moshi.Builder().add(CategoryCustomAdapter()).build()
    private val jokeResponseJsonAdapter : JsonAdapter<JokeResponse> = moshi.adapter(JokeResponse::class.java)
    private val categoryJsonCustomAdapter : JsonAdapter<CategoryResponse> = moshi.adapter(
        CategoryResponse::class.java)
    private val searchJsonAdapter : JsonAdapter<SearchResponse> = moshi.adapter(SearchResponse::class.java)

    //api object json
    protected val jokeResponseJson = ResourceReader("random_joke.json")
    protected val categoriesJson = ResourceReader("categories.json")
    protected val searchResponseJson = ResourceReader("search_response.json")
    protected val errorSearchResponseJson = ResourceReader("error_query.json")

    //base objects
    protected val mockJokeResponse = jokeResponseJsonAdapter.fromJson(jokeResponseJson)!!
    protected val mockCategoryResponse = categoryJsonCustomAdapter.fromJson(categoriesJson)!!
    protected val mockSearchResponse = searchJsonAdapter.fromJson(searchResponseJson)!!

    //base object fields
    protected val mockJokeResponseId = mockJokeResponse.id
    protected val mockJokeResponseJoke = mockJokeResponse.value
    protected val mockCategoriesCategories = this.mockCategoryResponse.categories!!
    protected val mockCategoriesCategory = this.mockCategoryResponse.categories!!.first()
    protected val mockCategoriesCategoryAll = Constants.CustomAttributes.CATEGORY_ALL
    protected val mockSearchResponseJokes = mockSearchResponse.result!!

    //private constants
    protected val KEY_LAST_CATEGORY_PICKED = "last_category_picked_key"
    protected val KEY_LAST_JOKE = "last_visualized_joke_key"
    protected val KEY_SELECTED_CATEGORY = "selected_category_result_key"
    protected val KEY_SELECTED_CATEGORY_STRING = "selected_category_string_value_key"


    //modified objects
    protected val mockJokeResponseFromCache = JokeResponse(
        mockJokeResponse,
        Constants.Sqlite.ID_LAST_VISUALIZED
    )
    val mockJokeResponseFavorited = JokeResponse(jokeResponse = mockJokeResponse, isFavorite = true)
    val mockJokeResponseFavoritedNewId = JokeResponse(id = "differentId", jokeResponse = mockJokeResponseFavorited)
    val categoriesListResponse = listOf(mockCategoriesCategoryAll,*mockCategoriesCategories.toTypedArray())
    val listFavoritedJokes = listOf(mockJokeResponseFavorited,mockJokeResponseFavoritedNewId)
    val lastVisualizedItem = LastVisualizedItem(
        itemId = Constants.Sqlite.ID_LAST_VISUALIZED,
        joke = mockJokeResponseFromCache
    )
    val liveDataFavoriteList : LiveData<List<JokeResponse>> =
        MutableLiveData(listFavoritedJokes)
    val liveDataEmptyJokeList : LiveData<List<JokeResponse>> = MutableLiveData()
    val emptySearchResponse = SearchResponse(result = emptyList(), total = 0)
    val networkStatusConnected = NetworkStatus.CONNECTED
    val networkStatusDisconnected = NetworkStatus.DISCONNECTED
    val liveDataNetworkConnected : LiveData<NetworkStatus> = MutableLiveData(networkStatusConnected)
    val liveDataNetworkDisconnected : LiveData<NetworkStatus> = MutableLiveData(networkStatusDisconnected)


    //input / output objects
    val idJokeResponseAddedItem = 1_000L
    val amountItemsRemovedJokeResponseDb = 1
    val checkIfFavoritedPositiveResponse = true
    val searchQuery = "strength"

    //wrapped objects
    val successfulCheckIfJokeIsFavoritedResponse =
        ResultChuck.Success(checkIfFavoritedPositiveResponse)
    val successfulAddedFavoritesResponse = ResultChuck.Success(idJokeResponseAddedItem)
    val successfulRemovedFavoritesResponse = ResultChuck.Success(amountItemsRemovedJokeResponseDb)
    val successfulCacheJokeResponse = ResultChuck.Success(data = mockJokeResponseFromCache)
    val successfulJokeResponse = ResultChuck.Success(data = mockJokeResponse)
    val successfulJokeResponseFavorited = ResultChuck.Success(mockJokeResponseFavorited)

    val successfulCategoriesList = ResultChuck.Success(data = categoriesListResponse)
    val successfulSearchResponse = ResultChuck.Success(data = mockSearchResponse)
    val successfulEmptySearchResponse = ResultChuck.Success(SearchResponse(result = listOf(),total = 0))
    val successfulSearchResponseEmptyList = ResultChuck.Success(emptySearchResponse)
    val successfulFavoriteListResponse = ResultChuck.Success(liveDataFavoriteList)
    val successfulEmptyFavoriteListResponse = ResultChuck.Success(liveDataEmptyJokeList)
    val errorUnknownJokeResponse = ResultChuck.Error<JokeResponse>(error = ErrorEntity.Unknown)
    val errorNotFoundFavoriteResponse = ResultChuck.Error<Number>(error = ErrorEntity.NotFound)
    fun <T> loadingResponse() : ResultChuck.Loading<out T> = ResultChuck.Loading(null)

    //network response
    val responseBody = "{}".toResponseBody("application/json".toMediaType())
    val httpException404 = HttpException(Response.error<HttpException>(404, responseBody))
    val errorBody = httpException404.convertErrorBody()
    fun <T> errorUnknown() = ResultChuck.Error<T>(error = ErrorEntity.Unknown)
    fun <T> errorNetwork() = ResultChuck.Error<T>(error = ErrorEntity.Network)
    fun <T> errorCacheNotFound() = ResultChuck.Error<T>(error = ErrorEntity.CacheNotFound)
    fun <T> result404Response() = ResultChuck.Error<T>(
        error = ErrorEntity.GenericError(
            httpException404.code(), httpException404.convertErrorBody()
        )
    )

    open fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        observeForever(observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}