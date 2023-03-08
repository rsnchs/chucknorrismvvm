package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleJokeToFavoritesTest : UseCaseBaseTest() {

    private lateinit var toggleJokeToFavorites: ToggleJokeToFavorites
    @MockK(relaxed = true) private lateinit var addJokeToFavorites: AddJokeToFavorites
    @MockK(relaxed = true) private lateinit var removeJokeFromFavorites: RemoveJokeFromFavorites

    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
        toggleJokeToFavorites = ToggleJokeToFavorites(addJokeToFavorites, removeJokeFromFavorites)
    }

    @Test
    fun `when joke is NOT favorite should trigger addJokeToFavorites`() = runTest {
        //act
        toggleJokeToFavorites(mockJokeResponse.copy(isFavorite = false))

        //assert
        coVerify { addJokeToFavorites(any()) }
        coVerify(exactly = 0) { removeJokeFromFavorites(any()) }
    }

    @Test
    fun `when joke is favorite should trigger removeJokeFromFavorites`() = runTest {
        //act
        toggleJokeToFavorites(mockJokeResponse.copy(isFavorite = true))

        //assert
        coVerify{ removeJokeFromFavorites(any()) }
        coVerify(exactly = 0) { addJokeToFavorites(any()) }
    }
}