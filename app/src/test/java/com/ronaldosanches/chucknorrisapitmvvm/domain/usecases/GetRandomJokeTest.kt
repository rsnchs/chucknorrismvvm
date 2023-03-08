package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetRandomJokeTest : UseCaseBaseTest() {

    private lateinit var  getRandomJoke : GetRandomJoke
    @MockK private lateinit var repository: ChuckNorrisJokesRepository

    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
        getRandomJoke = GetRandomJoke(repository)
    }

    @Test
    fun `when getting success answer from repository should emit`() = runTest {
        //arrange
        val expected = ResultChuck.Success(mockJokeResponse)
        coEvery { repository.getRandomJoke() } answers { expected }
        //act
        val actual = getRandomJoke()
        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when getting error answer from repository should emit`() = runTest {
        //arrange
        val expected = ResultChuck.Error<JokeResponse>(ErrorEntity.NotFound)
        coEvery { repository.getRandomJoke() } answers { expected }
        //act
        val actual = getRandomJoke()
        //assert
        assertEquals(expected, actual)
    }
}