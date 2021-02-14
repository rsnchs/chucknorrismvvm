package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetRandomJokeTest : UseCaseBaseTest() {

    lateinit var  getRandomJoke : GetRandomJoke

    @Before
    override fun setup() {
        super.setup()
        getRandomJoke = GetRandomJoke(repository)
    }

    @Test
    fun `should get random joke successful response from repository`() = runBlockingTest {
        //arrange
        whenever(getRandomJoke()).thenReturn(successfulJokeResponse)
        //act
        fun response() = suspend { getRandomJoke() }
        //assert
        assertEquals(response().invoke(),successfulJokeResponse)
        verify(repository).getRandomJoke()
    }

    @Test
    fun `should get error response from repository`() = runBlockingTest {
        //arrange
        whenever(getRandomJoke()).thenReturn(errorUnknownJokeResponse)
        //act
        fun response() = suspend { getRandomJoke() }
        //assert
        assertEquals(response().invoke(),errorUnknownJokeResponse)
        verify(repository).getRandomJoke()
    }
}