package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RemoveJokeFromFavoritesTest : UseCaseBaseTest() {

    private lateinit var removeJokeFromFavorites: RemoveJokeFromFavorites
    @MockK(relaxed = true) private lateinit var repository: ChuckNorrisJokesRepository
    @MockK private lateinit var checkIfJokeIsFavorite: CheckIfJokeIsFavorite

    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
        removeJokeFromFavorites = RemoveJokeFromFavorites(repository, checkIfJokeIsFavorite)
    }

    @Test
    fun `when starting class should trigger repository`() = runTest {
        //arrange
        val joke = mockJokeResponse
        coEvery { checkIfJokeIsFavorite(any()) } answers { ResultChuck.Success(true) }

        //act
        removeJokeFromFavorites(joke)

        //assert
        coVerify { repository.deleteJokeFromFavorites(joke) }
    }

    @Test
    fun `when checking if favorite returns error should return error`() = runTest {
        //arrange
        val expect = ResultChuck.Error<JokeResponse>(ErrorEntity.NotFound)
        coEvery { checkIfJokeIsFavorite(any()) } answers { ResultChuck.Error(ErrorEntity.NotFound) }

        //act
        val actual = removeJokeFromFavorites(mockJokeResponse)

        //assert
        assertEquals(expect, actual)
    }

    @Test
    fun `when checking if favorite returns success (true) should emit joke with favorite = true`() = runTest {
        //arrange
        val expect = ResultChuck.Success(mockJokeResponse.copy(isFavorite = true))
        coEvery { checkIfJokeIsFavorite(any()) } answers { ResultChuck.Success(true) }

        //act
        val actual = removeJokeFromFavorites(mockJokeResponse)

        //assert
        assertEquals(expect, actual)
    }

    @Test
    fun `when checking if favorite returns success (false) should emit joke with favorite = false`() = runTest {
        //arrange
        val expect = ResultChuck.Success(mockJokeResponse.copy(isFavorite = false))
        coEvery { checkIfJokeIsFavorite(any()) } answers { ResultChuck.Success(false) }

        //act
        val actual = removeJokeFromFavorites(mockJokeResponse)

        //assert
        assertEquals(expect, actual)
    }
}