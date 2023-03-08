package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCategoriesTest : UseCaseBaseTest() {

    private lateinit var getCategories : GetCategories
    @MockK private lateinit var repository: ChuckNorrisJokesRepository

    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
        getCategories = GetCategories(repository)
    }

    @Test
    fun `should get category successful response from repository`() = runTest {
        //arrange
        coEvery { repository.getCategories() } answers { successfulCategoriesList }
        val expected = successfulCategoriesList

        //act
        val actual = getCategories()

        //assert
        assertEquals(expected, actual)
    }
}