package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.data.models.ErrorItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetJokeBySearchTest : UseCaseBaseTest() {

    private lateinit var  getJokeBySearch: GetJokeBySearch
    @MockK private lateinit var repository: ChuckNorrisJokesRepository

    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
        getJokeBySearch = GetJokeBySearch(repository)
    }

    @Test
    fun `when getting successful joke search should emit value`() = runTest {
        //arrange
        val expected = ResultChuck.Success(successfulSearchResponse.data.result)
        coEvery { repository.getJokeBySearch(any()) } answers { successfulSearchResponse }

        //act
        val actual = getJokeBySearch("")

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when getting successful joke search empty should emit warning`() = runTest {
        //arrange
        val expected = ResultChuck.Success(listOf(WarningItem(R.string.no_results_returned, null)))
        coEvery { repository.getJokeBySearch(any()) } answers { ResultChuck.Success(emptySearchResponse) }

        //act
        val actual = getJokeBySearch("")

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when getting error joke search should emit value`() = runTest {
        //arrange
        val expected = ResultChuck.Success(listOf(ErrorItem(ErrorEntity.NotFound)))
        coEvery { repository.getJokeBySearch(any()) } answers { ResultChuck.Error(ErrorEntity.NotFound) }

        //act
        val actual = getJokeBySearch("")

        //assert
        assertEquals(expected, actual)
    }
}