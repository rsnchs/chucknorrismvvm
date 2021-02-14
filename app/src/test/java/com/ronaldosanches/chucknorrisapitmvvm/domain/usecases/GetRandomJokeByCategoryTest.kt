package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetRandomJokeByCategoryTest : UseCaseBaseTest() {

    lateinit var  getRandomJokeByCategory : GetRandomJokeByCategory

    @Before
    override fun setup() {
        super.setup()
        getRandomJokeByCategory = GetRandomJokeByCategory(repository)
    }

    @Test
    fun `should get random joke successful response from repository`() = runBlockingTest {
        //arrange
        whenever(repository.getRandomJokeByCategory(mockCategoriesCategory)).thenReturn(successfulJokeResponse)
        //act
        fun response() = suspend { getRandomJokeByCategory(mockCategoriesCategory) }
        //assert
        assertEquals(response().invoke(),successfulJokeResponse)
        verify(repository).getRandomJokeByCategory(mockCategoriesCategory)
    }

    @Test
    fun `should get error response from repository when error occurs`() = runBlockingTest {
        //arrange
        whenever(repository.getRandomJokeByCategory(mockCategoriesCategory)).thenReturn(errorUnknownJokeResponse)
        //act
        fun response() = suspend { getRandomJokeByCategory(mockCategoriesCategory) }
        //assert
        assertEquals(response().invoke(),errorUnknownJokeResponse)
        verify(repository).getRandomJokeByCategory(mockCategoriesCategory)
    }
}