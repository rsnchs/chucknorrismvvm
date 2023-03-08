package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddJokeToFavoritesTest : UseCaseBaseTest() {

    @MockK private lateinit var repository: ChuckNorrisJokesRepository
    @MockK private lateinit var checkIfJokeIsFavorite: CheckIfJokeIsFavorite
    private lateinit var addJokesToFavorites: AddJokeToFavorites


    @Before
    override fun setup() {
        MockKAnnotations.init(this)
        addJokesToFavorites = AddJokeToFavorites(repository, checkIfJokeIsFavorite)
        coEvery { repository.saveJokeToFavorites(any()) } answers { ResultChuck.Success(0L) }
        coEvery { checkIfJokeIsFavorite(any()) } answers { ResultChuck.Success(false) }
    }

    @Test
    fun `when start class should save joke and check if it's favorite`() = runTest {
        //act
        addJokesToFavorites(mockJokeResponse)

        coVerify { repository.saveJokeToFavorites(mockJokeResponse) }
        coVerify { checkIfJokeIsFavorite(mockJokeResponse.id) }
    }

    @Test
    fun `when check favorite returns error should emit error`() = runTest {
        //arrange
        coEvery { checkIfJokeIsFavorite(any()) } answers { ResultChuck.Error(ErrorEntity.NotFound)}
        val expected = ResultChuck.Error<JokeResponse>(ErrorEntity.NotFound)

        //act
        val actual = addJokesToFavorites(mockJokeResponse)

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when joke is favorite true should emit joke favorite true`() = runTest {
        //arrange
        coEvery { checkIfJokeIsFavorite(any()) } answers { ResultChuck.Success(true) }
        val expected = ResultChuck.Success(mockJokeResponse.copy(isFavorite = true))

        //act
        val actual = addJokesToFavorites(mockJokeResponse)

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when joke is favorite false should emit joke favorite false`() = runTest {
        //arrange
        coEvery { checkIfJokeIsFavorite(any()) } answers { ResultChuck.Success(false) }
        val expected = ResultChuck.Success(mockJokeResponse.copy(isFavorite = false))

        //act
        val actual = addJokesToFavorites(mockJokeResponse)

        //assert
        assertEquals(expected, actual)
    }
}