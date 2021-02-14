package com.ronaldosanches.chucknorrisapitmvvm.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.whenever
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiLocalDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiRemoteDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.models.LastVisualizedItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.verify
import java.io.IOException


@RunWith(JUnit4::class)
class ChuckNorrisJokesRepositoryImplTest : RepositoriesBaseTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var localDataSource: ChuckNorrisApiLocalDataSource
    lateinit var remoteDataSource: ChuckNorrisApiRemoteDataSource
    lateinit var repository: ChuckNorrisJokesRepositoryImpl
    lateinit var networkInfo: NetworkInfo

    @Before
    fun setup() {
        networkInfo = mock()
        localDataSource = mock()
        remoteDataSource = mock()
        repository = ChuckNorrisJokesRepositoryImpl(networkInfo, localDataSource, remoteDataSource)
    }

    @Test
    fun `repository interaction should check if device is connected`() = runBlockingTest {
        //arrange
        whenever(networkInfo.isConnected()).thenReturn(true)
        //act
        repository.getCategories()
        //assert
        verify(networkInfo).isConnected()
    }

    @Test
    fun `should return remote data when user online and remote call is successful`() = runBlockingTest {
        //arrange
        whenever(networkInfo.isConnected()).thenReturn(true)
        whenever(remoteDataSource.getRandomJoke()).thenReturn(mockJokeResponse)
        whenever(localDataSource.checkIfJokeIsFavorited(mockJokeResponseId)).thenReturn(true)
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), successfulJokeResponse)
        verify(remoteDataSource).getRandomJoke()
    }

    @Test
    fun `should return error network response when user online and remote call is unsuccessful`() = runBlockingTest {
        //arrange
        whenever(networkInfo.isConnected()).thenReturn(true)
        given(remoteDataSource.getRandomJoke()).willAnswer{ throw IOException() }
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), errorNetwork<JokeResponse>())
        verify(localDataSource, never()).saveJokeToFavorites(mockJokeResponse)
    }

    @Test
    fun `should return error generic response when user online and remote call returns 404`() = runBlockingTest {
        //arrange
        whenever(networkInfo.isConnected()).thenReturn(true)
        given(remoteDataSource.getRandomJoke()).willAnswer{ throw httpException404 }
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), result404Response<JokeResponse>())
        verify(localDataSource, never()).saveJokeToFavorites(mockJokeResponse)
    }

    @Test
    fun `should save last visualized joke with proper id when user online and remote call is successful`() = runBlockingTest {
        //arrange
        val lastVisualizedItem = LastVisualizedItem(joke = mockJokeResponse)
        whenever(networkInfo.isConnected()).thenReturn(true)
        whenever(remoteDataSource.getRandomJoke()).thenReturn(mockJokeResponse)
        whenever(localDataSource.checkIfJokeIsFavorited(mockJokeResponseId)).thenReturn(true)
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), successfulJokeResponse)
        verify(localDataSource).saveJokeToLastVisualized(lastVisualizedItem)
    }

    @Test
    fun `should return cache data when user is offline and local call is successful`() = runBlockingTest {
        //arrange
        whenever(networkInfo.isConnected()).thenReturn(false)
        whenever(localDataSource.getLastVisualizedJoke()).thenReturn(lastVisualizedItem)
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), successfulCacheJokeResponse)
        verify(localDataSource).getLastVisualizedJoke()
    }

    @Test
    fun `should return CacheException when user is offline and no cache is present`() = runBlockingTest {
        //arrange
        whenever(networkInfo.isConnected()).thenReturn(false)
        whenever(localDataSource.getLastVisualizedJoke()).thenReturn(null)
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), errorCacheNotFound<JokeResponse>())
        verify(localDataSource).getLastVisualizedJoke()
    }

    @Test
    fun `should return network error when user is online and call throws IOException`() = runBlockingTest {
        //arrange
        whenever(networkInfo.isConnected()).thenReturn(true)
        given(remoteDataSource.getRandomJoke()).willAnswer{ throw IOException() }
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), errorNetwork<CategoryResponse>())
        verify(remoteDataSource).getRandomJoke()
    }

    @Test
    fun `get random joke by category should return wrapped joke response`() = runBlockingTest {
        //assign
        whenever(networkInfo.isConnected()).thenReturn(true)
        whenever(remoteDataSource.getRandomJokeByCategory(mockCategoriesCategory)).thenReturn(mockJokeResponse)
        whenever(localDataSource.checkIfJokeIsFavorited(mockJokeResponseId)).thenReturn(true)
        //act
        fun result() = suspend { repository.getRandomJokeByCategory(mockCategoriesCategory) }
        //assert
        assertEquals(result().invoke(),successfulJokeResponse)
        verify(remoteDataSource).getRandomJokeByCategory(mockCategoriesCategory)
    }

    @Test
    fun `get joke by search should return wrapped search response`() = runBlockingTest {
        //assign
        whenever(networkInfo.isConnected()).thenReturn(true)
        whenever(remoteDataSource.getJokeBySearch(searchQuery)).thenReturn(mockSearchResponse)
        whenever(localDataSource.checkIfJokeIsFavorited(mockJokeResponseId)).thenReturn(true)
        //act
        fun result() = suspend { repository.getJokeBySearch(searchQuery) }
        //assert
        assertEquals(result().invoke(),successfulSearchResponse)
        verify(remoteDataSource).getJokeBySearch(searchQuery)
    }


    @Test
    fun `get joke by search when user is offline should return NetworkError`() = runBlockingTest {
        //assign
        whenever(networkInfo.isConnected()).thenReturn(false)
        //act
        fun result() = suspend { repository.getJokeBySearch(searchQuery) }
        //assert
        assertEquals(result().invoke(),errorNetwork<Any>())
    }

    @Test
    fun `get categories should return wrapped categories response plus all category`() = runBlockingTest {
        //assign
        whenever(networkInfo.isConnected()).thenReturn(true)
        whenever(remoteDataSource.getCategories()).thenReturn(mockCategoryResponse)
        whenever(localDataSource.checkIfJokeIsFavorited(mockJokeResponseId)).thenReturn(true)

        //act
        fun result() = suspend { repository.getCategories() }
        //assert
        assertEquals(result().invoke(),successfulCategoriesList)
        verify(remoteDataSource).getCategories()
    }

    @Test
    fun `adding joke to favorites should return successful item id wrapped`() = runBlockingTest {
        //assign
        whenever(localDataSource.saveJokeToFavorites(mockJokeResponse)).thenReturn(idJokeResponseAddedItem)
        //act
        fun result() = suspend { repository.saveJokeToFavorites(mockJokeResponse) }
        //assert
        assertEquals(result().invoke(),successfulAddedFavoritesResponse)
        verify(localDataSource).saveJokeToFavorites(mockJokeResponse)
    }

    @Test
    fun `removing joke from favorites should return amount of columns removed wrapped`() = runBlockingTest {
        //assign
        whenever(localDataSource.deleteJokeFromFavorites(mockJokeResponse)).thenReturn(amountItemsRemovedJokeResponseDb)
        //act
        fun result() = suspend { repository.deleteJokeFromFavorites(mockJokeResponse) }
        //assert
        assertEquals(result().invoke(),successfulRemovedFavoritesResponse)
        verify(localDataSource).deleteJokeFromFavorites(mockJokeResponse)
    }

    @Test
    fun `getting all jokes from favorites should return favorite joke list`() {
        val liveDataFavoritesList : LiveData<List<JokeResponse>> = MutableLiveData(listOf(mockJokeResponse))
        val successfulFavoriteListResponse = ResultChuck.Success(liveDataFavoritesList)
        //assign
        whenever(localDataSource.getFavoriteJokes()).thenReturn(liveDataFavoritesList)
        //act
        fun result(): () -> ResultChuck<LiveData<List<JokeResponse>>> = { repository.getFavoriteJokes() }
        //assert
        assertEquals(result().invoke(), successfulFavoriteListResponse)
        verify(localDataSource).getFavoriteJokes()
    }

    @Test
    fun `check if joke is favorited should return positive value wrapped`() = runBlockingTest {
        //assign
        whenever(localDataSource.checkIfJokeIsFavorited(mockJokeResponseId)).thenReturn(true)
        //act
        fun result() = suspend { repository.checkIfJokeIsFavorited(mockJokeResponseId) }
        //assert
        assertEquals(result().invoke(), successfulCheckIfJokeIsFavoritedResponse)
        verify(localDataSource).checkIfJokeIsFavorited(mockJokeResponseId)
    }
}