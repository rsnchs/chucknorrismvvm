package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokefavorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.ronaldosanches.chucknorrisapitmvvm.CoroutineTestRule
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetAllFavoriteJokes
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.RemoveJokeFromFavorites
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewmodels.ViewModelBaseTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers


class JokesListViewModelTest : ViewModelBaseTest() {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var jokesListViewModel: JokesListViewModel
    private lateinit var numberObserver: Observer<ResultChuck<Int>>
    private lateinit var viewTypeObserver: Observer<List<ViewType>>
    private lateinit var useCaseFavoriteJokes : GetAllFavoriteJokes
    private lateinit var useCaseRemoveJokeFromFavorites: RemoveJokeFromFavorites

    @Before
    fun setup() {
        numberObserver = mock()
        viewTypeObserver = mock()
        useCaseFavoriteJokes = mock()
        useCaseRemoveJokeFromFavorites = mock()
        jokesListViewModel = JokesListViewModel(useCaseFavoriteJokes,useCaseRemoveJokeFromFavorites)
    }

    @Test
    fun `calling all favorites should return favorite list wrapped`() {
        //arrange
        whenever(useCaseFavoriteJokes.invoke()).thenReturn(successfulFavoriteListResponse)
        //act
        val response = jokesListViewModel.showAllFavoriteJokes()
        //assert
        assertEquals(response,successfulFavoriteListResponse)
    }

    @Test
    fun `removing joke from favorites should return amount columns removed`() = runBlockingTest {
        //assign
        whenever(useCaseRemoveJokeFromFavorites(mockJokeResponse)).thenReturn(successfulRemovedFavoritesResponse)
        jokesListViewModel.removeFromFavories(mockJokeResponse).observeForever(numberObserver)
        //act
        val returnedAmountEntriesRemoved = jokesListViewModel.removeFromFavories(mockJokeResponse)
        //assert
        assertNotNull(returnedAmountEntriesRemoved)
        verify(numberObserver).onChanged(successfulRemovedFavoritesResponse)
        verifyNoMoreInteractions(numberObserver)
        assertEquals(returnedAmountEntriesRemoved.blockingObserve(),successfulRemovedFavoritesResponse)
    }


    @Test
    fun `adding not empty list to formatJokesList should create List of ViewType, first item title and JokeResponse after`() {
        //arrange
        jokesListViewModel.jokeList.observeForever(viewTypeObserver)
        //act
        val result = jokesListViewModel.formatJokesList(listOf(mockJokeResponse))
        //assert
        assertNotNull(result)
        verify(viewTypeObserver).onChanged(ArgumentMatchers.anyList())
        assertTrue(jokesListViewModel.jokeList.blockingObserve() is List<ViewType>)
        assertTrue(jokesListViewModel.jokeList.blockingObserve()?.first() is SectionTitleItem)
        assertTrue(jokesListViewModel.jokeList.blockingObserve()?.last() is JokeResponse)
    }

    @Test
    fun `adding empty list should return Warning item`() {
        //arrange
        jokesListViewModel.jokeList.observeForever(viewTypeObserver)
        //act
        val result = jokesListViewModel.formatJokesList(listOf())
        //assert
        assertNotNull(result)
        verify(viewTypeObserver).onChanged(ArgumentMatchers.anyList())
        assertTrue(jokesListViewModel.jokeList.blockingObserve() is List<ViewType>)
        assertTrue(jokesListViewModel.jokeList.blockingObserve()?.first() is WarningItem)
    }
}