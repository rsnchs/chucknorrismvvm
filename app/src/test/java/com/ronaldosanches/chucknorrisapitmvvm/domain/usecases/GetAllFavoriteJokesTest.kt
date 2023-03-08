package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.data.models.ErrorItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAllFavoriteJokesTest : UseCaseBaseTest() {

    private lateinit var getAllFavorites: GetAllFavoriteJokes
    @MockK private lateinit var repository: ChuckNorrisJokesRepository


    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
        getAllFavorites = GetAllFavoriteJokes(repository)
    }

    @Test
    fun `when getting favorite and it's not empty should emit list of jokes`() = runTest {
        //arrange
        val expected = ResultChuck.Success(listOf(mockJokeResponse))
        coEvery { repository.getFavoriteJokes() } answers { expected }

        //act
        val actual = getAllFavorites()

        //assert
        assertEquals(expected, actual)
    }
    @Test
    fun `when getting favorite and it's empty should emit response`() = runTest {
        //arrange
        val expected = ResultChuck.Success(listOf(WarningItem()))
        coEvery { repository.getFavoriteJokes() } answers { ResultChuck.Success(emptyList()) }

        //act
        val actual = getAllFavorites()

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when getting favorite and it's error should emit response`() = runTest {
        //arrange
        val expected = ResultChuck.Success(listOf(ErrorItem(ErrorEntity.NotFound)))
        coEvery { repository.getFavoriteJokes() } answers { ResultChuck.Error(ErrorEntity.NotFound) }

        //act
        val actual = getAllFavorites()

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when getting favorite and it's loading should empty list`() = runTest {
        //arrange
        val expected = ResultChuck.Success(listOf<ViewType>())
        coEvery { repository.getFavoriteJokes() } answers { ResultChuck.Loading() }

        //act
        val actual = getAllFavorites()

        //assert
        assertEquals(expected, actual)
    }
}