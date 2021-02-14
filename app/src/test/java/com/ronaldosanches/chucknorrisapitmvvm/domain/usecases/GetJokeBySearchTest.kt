package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetJokeBySearchTest : UseCaseBaseTest() {

    lateinit var  getJokeBySearch : GetJokeBySearch

    @Before
    override fun setup() {
        super.setup()
        getJokeBySearch = GetJokeBySearch(repository)
    }

    @Test
    fun `should get random joke successful response from repository when succesful response occurs`() = runBlockingTest {
        //arrange
        whenever(getJokeBySearch(searchQuery)).thenReturn(successfulSearchResponse)
        //act
        fun response() = suspend { getJokeBySearch(searchQuery) }
        //assert
        assertEquals(response().invoke(),successfulSearchResponse)
        verify(repository).getJokeBySearch(searchQuery)
    }

    @Test
    fun `should get error response from repository when error occur`() = runBlockingTest {
        //arrange
        whenever(getJokeBySearch(searchQuery)).thenReturn(errorUnknown())
        //act
        fun response() = suspend { getJokeBySearch(searchQuery) }
        //assert
        assertEquals(response().invoke(),errorUnknown<SearchResponse>())
        verify(repository).getJokeBySearch(searchQuery)
    }
}