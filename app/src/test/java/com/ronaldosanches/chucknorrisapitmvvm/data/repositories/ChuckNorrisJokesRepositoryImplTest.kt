package com.ronaldosanches.chucknorrisapitmvvm.data.repositories

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiLocalDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.datasources.ChuckNorrisApiRemoteDataSource
import com.ronaldosanches.chucknorrisapitmvvm.data.models.LastVisualizedItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException


class ChuckNorrisJokesRepositoryImplTest : RepositoriesBaseTest() {
    private lateinit var repository: ChuckNorrisJokesRepositoryImpl
    @MockK private lateinit var localDataSource: ChuckNorrisApiLocalDataSource
    @MockK private lateinit var remoteDataSource: ChuckNorrisApiRemoteDataSource
    @MockK private lateinit var networkInfo: NetworkInfo

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = ChuckNorrisJokesRepositoryImpl(networkInfo, localDataSource, remoteDataSource)
        coEvery { networkInfo.isConnected() } answers { true }
    }

    //get random joke
    //user is offline
    @Test
    fun `online = false and get random`() = runTest {
        //arrange
        coEvery { networkInfo.isConnected() } answers { false }
        coEvery { localDataSource.getLastVisualizedJoke() } answers { lastVisualizedItem }
        //act
        val actual = repository.getRandomJoke()

        //assert

    }

    @Test
    fun `repository interaction should check if device is connected`() = runTest {
        //arrange
        coEvery { networkInfo.isConnected() } answers { true }
        //act
        repository.getCategories()
        //assert
        coVerify { networkInfo.isConnected() }
    }

    @Test
    fun `should return remote data when user online and remote call is successful`() = runTest {
        //arrange
        coEvery { networkInfo.isConnected() } answers { true }
        coEvery { remoteDataSource.getRandomJoke() } answers { mockJokeResponse }
        coEvery { localDataSource.checkIfJokeIsFavorited(mockJokeResponseId) } answers { true }
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), successfulJokeResponse)
        coVerify { remoteDataSource.getRandomJoke() }
    }

    @Test
    fun `should return error network response when user online and remote call is unsuccessful`() = runTest {
        //arrange
        coEvery { networkInfo.isConnected() } answers { true }
        coEvery { remoteDataSource.getRandomJoke() } throws(IOException())
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), errorNetwork<JokeResponse>())
        coVerify(exactly = 0) { localDataSource.saveJokeToFavorites(mockJokeResponse) }
    }

    @Test
    fun `should return error generic response when user online and remote call returns 404`() = runTest {
        //arrange
        coEvery { networkInfo.isConnected() } answers { true }
        coEvery { remoteDataSource.getRandomJoke() } throws (httpException404)
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), result404Response<JokeResponse>())
        coVerify(exactly = 0) { localDataSource.saveJokeToFavorites(mockJokeResponse) }
    }

    @Test
    fun `should save last visualized joke with proper id when user online and remote call is successful`() = runTest {
        //arrange
        val lastVisualizedItem = LastVisualizedItem(joke = mockJokeResponse)
        coEvery { networkInfo.isConnected() } answers { true }
        coEvery { remoteDataSource.getRandomJoke() } answers { mockJokeResponse }
        coEvery { localDataSource.checkIfJokeIsFavorited(mockJokeResponseId) } answers { true }
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), successfulJokeResponse)
        coVerify { localDataSource.saveJokeToLastVisualized(lastVisualizedItem) }
    }

    @Test
    fun `should return cache data when user is offline and local call is successful`() = runTest {
        //arrange
        coEvery { networkInfo.isConnected() } answers { false }
        coEvery { localDataSource.getLastVisualizedJoke() } answers { lastVisualizedItem }
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), successfulCacheJokeResponse)
        coVerify { localDataSource.getLastVisualizedJoke() }
    }

    @Test
    fun `should return CacheException when user is offline and no cache is present`() = runTest {
        //arrange
        coEvery { networkInfo.isConnected() } answers { false }
        coEvery { localDataSource.getLastVisualizedJoke() } answers { null }
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), errorCacheNotFound<JokeResponse>())
        coVerify { localDataSource.getLastVisualizedJoke() }
    }

    @Test
    fun `should return network error when user is online and call throws IOException`() = runTest {
        //arrange
        coEvery { networkInfo.isConnected() } answers { true }
        coEvery { remoteDataSource.getRandomJoke() } throws(IOException())
        //act
        fun result() = suspend { repository.getRandomJoke() }
        //assert
        assertEquals(result().invoke(), errorNetwork<CategoryResponse>())
        coVerify { remoteDataSource.getRandomJoke() }
    }

    @Test
    fun `get random joke by category should return wrapped joke response`() = runTest {
        //assign
        coEvery { networkInfo.isConnected() } answers { true }
        coEvery { remoteDataSource.getRandomJokeByCategory(mockCategoriesCategory) } answers { mockJokeResponse }
        coEvery { localDataSource.checkIfJokeIsFavorited(mockJokeResponseId) } answers { true }
        //act
        fun result() = suspend { repository.getRandomJokeByCategory(mockCategoriesCategory) }
        //assert
        assertEquals(result().invoke(),successfulJokeResponse)
        coVerify { remoteDataSource.getRandomJokeByCategory(mockCategoriesCategory) }
    }

    @Test
    fun `get joke by search should return wrapped search response`() = runTest {
        //assign
        coEvery { networkInfo.isConnected() } answers { true }
        coEvery { remoteDataSource.getJokeBySearch(searchQuery) } answers { mockSearchResponse }
        coEvery { localDataSource.checkIfJokeIsFavorited(mockJokeResponseId) } answers { true }
        //act
        fun result() = suspend { repository.getJokeBySearch(searchQuery) }
        //assert
        assertEquals(result().invoke(),successfulSearchResponse)
        coVerify { remoteDataSource.getJokeBySearch(searchQuery) }
    }


    @Test
    fun `get joke by search when user is offline should return NetworkError`() = runTest {
        //assign
        coEvery { networkInfo.isConnected() } answers { false }
        //act
        fun result() = suspend { repository.getJokeBySearch(searchQuery) }
        //assert
        assertEquals(result().invoke(),errorNetwork<Any>())
    }

    @Test
    fun `get categories should return wrapped categories response plus all category`() = runTest {
        //assign
        coEvery { networkInfo.isConnected() } answers { true }
        coEvery { remoteDataSource.getCategories() } answers { mockCategoryResponse }
        coEvery { localDataSource.checkIfJokeIsFavorited(mockJokeResponseId) } answers { true }

        //act
        fun result() = suspend { repository.getCategories() }
        //assert
        assertEquals(result().invoke(),successfulCategoriesList)
        coVerify { remoteDataSource.getCategories() }
    }

    @Test
    fun `adding joke to favorites should return successful item id wrapped`() = runTest {
        //assign
        coEvery { localDataSource.saveJokeToFavorites(mockJokeResponse) } answers { idJokeResponseAddedItem }
        //act
        fun result() = suspend { repository.saveJokeToFavorites(mockJokeResponse) }
        //assert
        assertEquals(result().invoke(),successfulAddedFavoritesResponse)
        coVerify { localDataSource.saveJokeToFavorites(mockJokeResponse) }
    }

    @Test
    fun `removing joke from favorites should return amount of columns removed wrapped`() = runTest {
        //assign
        coEvery { (localDataSource.deleteJokeFromFavorites(mockJokeResponse)) } answers { amountItemsRemovedJokeResponseDb }
        //act
        fun result() = suspend { repository.deleteJokeFromFavorites(mockJokeResponse) }
        //assert
        assertEquals(result().invoke(),successfulRemovedFavoritesResponse)
        coVerify { localDataSource.deleteJokeFromFavorites(mockJokeResponse) }
    }

    @Test
    fun `getting all jokes from favorites should return favorite joke list`() {
        val favoriteListMock : List<JokeResponse> = listOf(mockJokeResponse)
        val successfulFavoriteListResponse = ResultChuck.Success(favoriteListMock)
        //assign
        coEvery { localDataSource.getFavoriteJokes() } answers { favoriteListMock }
        //act
        fun result(): () -> ResultChuck<List<JokeResponse>> = { repository.getFavoriteJokes() }
        //assert
        assertEquals(result().invoke(), successfulFavoriteListResponse)
        coVerify { localDataSource.getFavoriteJokes() }
    }

    @Test
    fun `check if joke is favorited should return positive value wrapped`() = runTest {
        //assign
        coEvery { localDataSource.checkIfJokeIsFavorited(mockJokeResponseId) } answers { true }
        //act
        fun result() = suspend { repository.checkIfJokeIsFavorited(mockJokeResponseId) }
        //assert
        assertEquals(result().invoke(), successfulCheckIfJokeIsFavoritedResponse)
        coVerify { localDataSource.checkIfJokeIsFavorited(mockJokeResponseId) }
    }
}