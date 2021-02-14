package com.ronaldosanches.chucknorrisapitmvvm.data.datasources

import org.junit.Assert.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.net.HttpURLConnection


class ChuckNorrisApiRemoteDataSourceTest : DataSourceBaseTest() {

    @get:Rule
    var exception: ExpectedException = ExpectedException.none()

    private val mockedServer = MockWebServer()
    private lateinit var remoteDataSource : ChuckNorrisApiRemoteDataSource

    @Before
    fun setup() {
        mockedServer.start()
        remoteDataSource = Retrofit.Builder()
                .baseUrl(mockedServer.url("/"))
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ChuckNorrisApiRemoteDataSource::class.java)
    }

    @After
    fun closure() {
        mockedServer.close()
    }

    @Test
    fun `resource should be properly loaded`(){
        //assert
        assertNotNull(jokeResponseJson)
        assertNotNull(categoriesJson)
        assertNotNull(searchResponseJson)
        assertNotNull(errorSearchResponseJson)
    }

    @Test
    fun `successful random Joke remote fetch should return valid response`() = runBlocking {
        //assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jokeResponseJson)

        //act
        mockedServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()
        val actualResponse = remoteDataSource.getRandomJoke()

        //assert
        assertEquals(mockResponse?.let { mockJokeResponse },
                actualResponse)
    }

    @Test
    fun `successful random Joke by category remote fetch should return valid response`() = runBlocking {
        //assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jokeResponseJson)

        //act
        mockedServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()
        val actualResponse = remoteDataSource.getRandomJokeByCategory(category = mockCategoriesCategory)
        //assert

        assertEquals(mockResponse?.let { mockJokeResponse },
                actualResponse)
    }

    @Test
    fun `successful search remote fetch should return valid response`() = runBlocking {
        //assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(searchResponseJson)

        //act
        mockedServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()
        val actualResponse = remoteDataSource.getJokeBySearch(search = searchQuery)
        //assert

        assertEquals(mockResponse?.let { this@ChuckNorrisApiRemoteDataSourceTest.mockSearchResponse },
                actualResponse)
    }


    @Test
    fun `successful categories remote fetch should return valid response`() = runBlocking {

        //assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(categoriesJson)

        //act
        mockedServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()
        val actualResponse = remoteDataSource.getCategories()
        //assert

        assertEquals(mockResponse?.let { this@ChuckNorrisApiRemoteDataSourceTest.mockCategoryResponse },
                actualResponse)
    }

    @Test
    fun `bad request search should return HttpException`() = runBlocking {
        var exception : Throwable? = null
        //assign
        val response = MockResponse()
                .setResponseCode(400).setBody(errorSearchResponseJson)
        //act
        mockedServer.enqueue(response)
        response.getBody()?.readUtf8()
        try {
            remoteDataSource.getJokeBySearch(search = searchQuery)
        } catch (e: Throwable) {
            exception = e
        }
        assertNotNull(exception)
        assertTrue(exception is HttpException)
    }

    @Test
    fun `no connection request should return IOException`() = runBlocking {
        var exception : Throwable? = null
        //assign
        val mockResponse = MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        //act
        mockedServer.enqueue(mockResponse)
        mockResponse.getBody()?.readUtf8()
        try {
            remoteDataSource.getJokeBySearch(search = searchQuery)
        } catch (e: Throwable) {
            exception = e
        }
        //assert
        assertNotNull(exception)
        assertTrue(exception is IOException)
    }
}