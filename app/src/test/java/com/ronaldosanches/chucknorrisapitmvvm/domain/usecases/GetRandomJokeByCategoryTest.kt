package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRandomJokeByCategoryTest : UseCaseBaseTest() {

    private lateinit var  getRandomJokeByCategory : GetRandomJokeByCategory
    @MockK private lateinit var repository: ChuckNorrisJokesRepository

    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
        getRandomJokeByCategory = GetRandomJokeByCategory(repository)
    }

    @Test
    fun `when getting successful joke by categorie should emit result`() = runTest {
        //arrange
        val expected = ResultChuck.Success(mockJokeResponse)
        coEvery { repository.getRandomJokeByCategory(any()) } answers { expected }
        //act
        val actual = getRandomJokeByCategory("")

        //assert
        assertEquals(expected, actual)
    }
}