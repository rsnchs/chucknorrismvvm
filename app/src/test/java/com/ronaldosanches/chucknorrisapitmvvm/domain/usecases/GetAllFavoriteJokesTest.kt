package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetAllFavoriteJokesTest : UseCaseBaseTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var getAllFavorites: GetAllFavoriteJokes

    @Before
    override fun setup() {
        super.setup()
        getAllFavorites = GetAllFavoriteJokes(repository)
    }

    @Test
    fun `trigger method should return successfully list of jokes`() = runBlockingTest {
        //arrange
        whenever(repository.getFavoriteJokes()).thenReturn(successfulFavoriteListResponse)
        //act
        fun response(): () -> ResultChuck<LiveData<List<JokeResponse>>> = { getAllFavorites() }
        //assert
        assertEquals(response().invoke(), successfulFavoriteListResponse)
        verify(repository).getFavoriteJokes()
    }

    @Test
    fun `trigger method should return empty list`() = runBlockingTest {
        //arrange
        whenever(repository.getFavoriteJokes()).thenReturn(successfulEmptyFavoriteListResponse)
        //act
        fun response(): () -> ResultChuck<LiveData<List<JokeResponse>>> = { getAllFavorites() }
        //assert
        assertEquals(response().invoke(), successfulEmptyFavoriteListResponse)
        verify(repository).getFavoriteJokes()
    }
}