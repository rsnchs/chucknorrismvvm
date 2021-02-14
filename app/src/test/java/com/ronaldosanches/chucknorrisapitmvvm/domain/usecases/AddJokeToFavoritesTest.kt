package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class AddJokeToFavoritesTest : UseCaseBaseTest() {

    lateinit var addJokesToFavorites: AddJokeToFavorites

    @Before
    override fun setup() {
        super.setup()
        addJokesToFavorites = AddJokeToFavorites(repository)
    }

    @Test
    fun `adding joke to favorites should return added item id and trigger method`() = runBlockingTest {
        //arrange
        whenever(repository.saveJokeToFavorites(mockJokeResponse)).thenReturn(successfulAddedFavoritesResponse)
        //act
        fun response() = suspend { addJokesToFavorites(mockJokeResponse) }
        //assert
        assertEquals(response().invoke(),successfulAddedFavoritesResponse)
        verify(repository).saveJokeToFavorites(any())
    }
}