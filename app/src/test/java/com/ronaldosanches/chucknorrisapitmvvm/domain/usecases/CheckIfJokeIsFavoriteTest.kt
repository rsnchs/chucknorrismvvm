package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CheckIfJokeIsFavoriteTest : UseCaseBaseTest() {

    private lateinit var checkIfJokeIsFavorite: CheckIfJokeIsFavorite
    @MockK private lateinit var repository: ChuckNorrisJokesRepository

    @Before
    override fun setup() {
        MockKAnnotations.init(this)
        checkIfJokeIsFavorite = CheckIfJokeIsFavorite(repository)
    }

    @Test
    fun `when checking if joke id is favorite true should return true`() = runTest {
        //arrange
        val expected = ResultChuck.Success(true)
        coEvery { repository.checkIfJokeIsFavorited(any()) } answers { expected }

        //act
        val actual = checkIfJokeIsFavorite("")

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when checking if joke id is favorite false should return false`() = runTest {
        //arrange
        val expected = ResultChuck.Success(false)
        coEvery { repository.checkIfJokeIsFavorited(any()) } answers { expected }

        //act
        val actual = checkIfJokeIsFavorite("")

        //assert
        assertEquals(expected, actual)
    }
}