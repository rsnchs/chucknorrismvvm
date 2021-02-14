package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class CheckIfJokeIsFavoritedTest : UseCaseBaseTest() {

    lateinit var checkIfJokeIsFavorited: CheckIfJokeIsFavorited

    @Before
    override fun setup() {
        super.setup()
        checkIfJokeIsFavorited = CheckIfJokeIsFavorited(repository)
    }

    @Test
    fun `checking if id is favorite should return positive value`() = runBlockingTest {
        //arrange
        whenever(repository.checkIfJokeIsFavorited(mockJokeResponseId))
                .thenReturn(successfulCheckIfJokeIsFavoritedResponse)
        //act
        fun response() = suspend { checkIfJokeIsFavorited(mockJokeResponseId) }
        //assert
        assertEquals(response().invoke(),successfulCheckIfJokeIsFavoritedResponse)
        verify(repository).checkIfJokeIsFavorited(any())
    }
}