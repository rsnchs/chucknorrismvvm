package com.ronaldosanches.chucknorrisapitmvvm.data.datasources

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ronaldosanches.chucknorrisapitmvvm.ObjectResources
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.data.models.LastVisualizedItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChuckNorrisApiLocalDataSourceTest : ObjectResources() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao : ChuckNorrisApiLocalDataSource
    private lateinit var db : LocalDataBase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context,LocalDataBase::class.java)
                .allowMainThreadQueries().build()
        dao = db.jokeDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun adding_joke_to_db_should_be_retrievable() = runBlockingTest {
        //act
        val insertId = dao.saveJokeToFavorites(mockJokeResponse)
        val result = dao.getJokeById(mockJokeResponseId)
        //assert
        assertNotNull(insertId)
        assertEquals(result?.id, mockJokeResponseId)
    }


    @Test
    fun delete_joke_from_db_should_return_number_of_affected_rows() = runBlockingTest {
        //act
        dao.saveJokeToFavorites(mockJokeResponse)
        val result = dao.deleteJokeFromFavorites(mockJokeResponse)
        //assert
        assertEquals(result,amountItemsRemovedJokeResponseDb)
    }

    @Test
    fun db_query_should_return_right_number_of_entries() = runBlockingTest {
        //assign
        val randomResponse1 = JokeResponse(
                categories = listOf( "category 1"),
                createdAt = "created at 1",
                iconURL = "icon url 1",
                updatedAt = "updated at 1",
                url = "url 1",
                value = "joke 1",
                id = "id 1",
                isFavorite = false
        )

        val randomResponse2 = JokeResponse(
                categories = listOf( "category 2"),
                createdAt = "created at 2",
                iconURL = "icon url 2",
                updatedAt = "updated at 2",
                url = "url 2",
                value = "joke 2",
                id = "id 2",
                isFavorite = false
        )

        val randomResponseLastVisualized = JokeResponse(
                categories = listOf( "category 1"),
                createdAt = "created at 2",
                iconURL = "icon url 2",
                updatedAt = "updated at 2",
                url = "url 2",
                value = "joke 2",
                id = "id 3",
                isFavorite = false
        )

        val lastVisualizedItem = LastVisualizedItem(joke = randomResponseLastVisualized,
                itemId = Constants.Sqlite.ID_LAST_VISUALIZED)


        //act
        dao.saveJokeToFavorites(randomResponse1)
        dao.saveJokeToFavorites(randomResponse2)
        dao.saveJokeToLastVisualized(lastVisualizedItem)
        val allJokesList = dao.getFavoriteJokes().blockingObserve()
        val getLastVisualizedJoke = dao.getLastVisualizedJoke()
        // assert
        assertEquals(allJokesList, listOf(randomResponse1,randomResponse2))
        assertEquals(lastVisualizedItem,getLastVisualizedJoke)
    }

    @Test
    fun adding_more_than_one_joke_to_last_visualized_joke_should_return_last_joke_added() = runBlockingTest {
        //assign
        val randomResponseLastVisualized1 = JokeResponse(
                categories = listOf( "category 1"),
                createdAt = "created at 2",
                iconURL = "icon url 2",
                updatedAt = "updated at 2",
                url = "url 2",
                value = "joke 2",
                id = "id 3",
                isFavorite = false
        )

        val randomResponseLastVisualized2 = JokeResponse(
                categories = listOf( "category 2"),
                createdAt = "created at 2",
                iconURL = "icon url 2",
                updatedAt = "updated at 2",
                url = "url 2",
                value = "joke 2",
                id = "id 2",
                isFavorite = false
        )

        val lastVisualizedItem1 = LastVisualizedItem(joke = randomResponseLastVisualized1,
                itemId = Constants.Sqlite.ID_LAST_VISUALIZED)

        val lastVisualizedItem2 = LastVisualizedItem(joke = randomResponseLastVisualized2,
                itemId = Constants.Sqlite.ID_LAST_VISUALIZED)

        //act
        dao.saveJokeToLastVisualized(lastVisualizedItem1)
        dao.saveJokeToLastVisualized(lastVisualizedItem2)
        val lastJokeResponse = dao.getLastVisualizedJoke()
        //assert
        assertEquals(lastVisualizedItem2,lastJokeResponse)
    }

    @Test
    fun not_found_objects_should_return_proper_values() = runBlockingTest {
        //assign
        val randomResponse = JokeResponse(
                categories = listOf( "dev"),
                createdAt = "2020-01-05 13:42:20.841843",
                iconURL = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                updatedAt = "2020-01-05 13:42:20.841843",
                url = "https://api.chucknorris.io/jokes/4FzC21P8TkqUEBM5vVKd3A",
                value = "Chuck Norris once did a woman so hard she had to change churches.",
                id = "4FzC21P8TkqUEBM5vVKd3A",
                isFavorite = false
        )
        val unknownId = "unknown_id"
        //act
        val zeroRowsAffected = dao.deleteJokeFromFavorites(randomResponse)
        val  notFoundJoke = dao.getJokeById(unknownId)
        val favoritesList = dao.getFavoriteJokes().blockingObserve()
        //assert
        assertEquals(zeroRowsAffected,0)
        assertEquals(notFoundJoke,null)
        assertEquals(favoritesList, listOf<JokeResponse>())
    }

    @Test
    fun checking_if_joke_is_favorited_should_return_value() = runBlockingTest {
        //act
        dao.saveJokeToFavorites(mockJokeResponse)
        val validId = dao.checkIfJokeIsFavorited(mockJokeResponseId)
        val invalidId = dao.checkIfJokeIsFavorited("invalid_id")
        //assert
        assertTrue(validId)
        assertFalse(invalidId)
    }

    @Test
    fun getting_joke_by_id_should_return_value() = runBlockingTest {
        //act
        dao.saveJokeToFavorites(mockJokeResponse)
        val validSearchQuery = dao.getJokeById(mockJokeResponseId)
        val invalidSearchQuery = dao.getJokeById("invalid_id")
        //assert
        assertEquals(validSearchQuery, mockJokeResponse)
        assertEquals(invalidSearchQuery, null)
    }
}