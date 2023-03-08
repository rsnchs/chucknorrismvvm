package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeOptionsMenu
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class GetJokeOptionsMenuTest : UseCaseBaseTest() {

    private lateinit var getJokeOptionsMenu: GetJokeOptionsMenu

    @Before
    override fun setup() {
        super.setup()
        getJokeOptionsMenu = GetJokeOptionsMenu()
    }

    @Test
    fun `when getting list should emit values`() = runTest {
        //arrange
        val expected = listOf(
            JokeOptionsMenu.CHOOSE_CATEGORY,
            JokeOptionsMenu.SHOW_ALL_FAVORITE_JOKES,
        )
        //act
        val actual = getJokeOptionsMenu()

        //assert
        assertEquals(expected, actual)
    }
}