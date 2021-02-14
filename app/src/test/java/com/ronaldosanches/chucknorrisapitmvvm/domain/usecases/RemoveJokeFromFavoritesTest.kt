package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class RemoveJokeFromFavoritesTest : UseCaseBaseTest() {

    lateinit var removeJokeFromFavorites: RemoveJokeFromFavorites

    @Before
    override fun setup() {
        super.setup()
        removeJokeFromFavorites = RemoveJokeFromFavorites(repository)
    }

    @Test
    fun `removing one joke from favorites should return amount of items removed and trigger method`() = runBlockingTest {
        //arrange
        whenever(repository.deleteJokeFromFavorites(mockJokeResponse)).thenReturn(successfulRemovedFavoritesResponse)
        //act
        fun response() = suspend { removeJokeFromFavorites(mockJokeResponse) }
        //assert
        assertEquals(response().invoke(),successfulRemovedFavoritesResponse)
        verify(repository).deleteJokeFromFavorites(mockJokeResponse)
    }
}