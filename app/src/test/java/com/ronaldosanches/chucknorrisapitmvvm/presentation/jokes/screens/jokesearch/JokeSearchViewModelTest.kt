package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokesearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.ronaldosanches.chucknorrisapitmvvm.CoroutineTestRule
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.AddJokeToFavorites
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetJokeBySearch
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.RemoveJokeFromFavorites
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewmodels.ViewModelBaseTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList

class JokeSearchViewModelTest : ViewModelBaseTest() {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var jokeSearchViewModel: JokeSearchViewModel
    private lateinit var jokeBySearchObserver: Observer<ResultChuck<SearchResponse>>
    private lateinit var viewTypeObserver: Observer<List<ViewType>>
    private lateinit var numberObserver: Observer<ResultChuck<out Number>>
    private lateinit var useCaseGetJokeBySearch : GetJokeBySearch
    private lateinit var useCaseAddJokesToFavorites : AddJokeToFavorites
    private lateinit var useCaseRemoveJokeFromFavorites: RemoveJokeFromFavorites

    @Before
    fun setup() {
        jokeBySearchObserver = mock()
        viewTypeObserver = mock()
        numberObserver = mock()
        useCaseGetJokeBySearch = mock()
        useCaseAddJokesToFavorites = mock()
        useCaseRemoveJokeFromFavorites = mock()
        jokeSearchViewModel = spy(
            JokeSearchViewModel(useCaseGetJokeBySearch,
                useCaseAddJokesToFavorites,useCaseRemoveJokeFromFavorites)
        )
    }

    @Test
    fun `using query should return valid search result wrapped`() = runBlockingTest {
        //arrange
        whenever(useCaseGetJokeBySearch(searchQuery)).thenReturn(successfulSearchResponse)
        jokeSearchViewModel.getJokeBySearch(searchQuery).observeForever(jokeBySearchObserver)
        //act
        val liveData = jokeSearchViewModel.getJokeBySearch(searchQuery)
        //assert
        assertNotNull(liveData)
        verify(jokeBySearchObserver).onChanged(ResultChuck.Loading(null))
        verify(jokeBySearchObserver).onChanged(successfulSearchResponse)
        verifyNoMoreInteractions(jokeBySearchObserver)
    }

    @Test
    fun `adding non empty search list response should return view type list for joke item`() {
        //arrange
        jokeSearchViewModel.jokeList.observeForever(viewTypeObserver)
        //act
        jokeSearchViewModel.handleSearchResponse(successfulSearchResponse)
        //assert
        verify(viewTypeObserver).onChanged(anyList())
        assertTrue(jokeSearchViewModel.jokeList.blockingObserve() is List<ViewType>)
        assertTrue(jokeSearchViewModel.jokeList.blockingObserve()?.first() is SectionTitleItem)
        assertTrue(jokeSearchViewModel.jokeList.blockingObserve()?.last() is JokeResponse)
        verifyNoMoreInteractions(viewTypeObserver)
    }

    @Test
    fun `adding empty search list response should return view type list for empty case`() {
        //arrange
        jokeSearchViewModel.jokeList.observeForever(viewTypeObserver)
        //act
        jokeSearchViewModel.handleSearchResponse(successfulSearchResponseEmptyList)
        //assert
        verify(viewTypeObserver).onChanged(anyList())
        assertTrue(jokeSearchViewModel.jokeList.blockingObserve() is List<ViewType>)
        assertTrue(jokeSearchViewModel.jokeList.blockingObserve()?.first() is WarningItem)
        verifyNoMoreInteractions(viewTypeObserver)
    }

    @Test
    fun `on joke not favorited click should return id type Long of added item to favorites`() = runBlockingTest {
        //assign
        whenever(useCaseAddJokesToFavorites(mockJokeResponse)).thenReturn(successfulAddedFavoritesResponse)
        jokeSearchViewModel.handleFavoriteButtonClick(mockJokeResponse).observeForever(numberObserver)
        //act
        val liveData = jokeSearchViewModel.handleFavoriteButtonClick(mockJokeResponse)
        //assert
        assertNotNull(liveData)
        verify(numberObserver).onChanged(loadingResponse())
        verify(numberObserver).onChanged(successfulAddedFavoritesResponse)
        verifyNoMoreInteractions(numberObserver)
        assertEquals(liveData.blockingObserve(),successfulAddedFavoritesResponse)
    }

    @Test
    fun `on joke favorited click should return amount of columns deleted from db`() = runBlockingTest {
        //assign
        whenever(useCaseRemoveJokeFromFavorites(mockJokeResponseFavorited)).thenReturn(successfulRemovedFavoritesResponse)
        jokeSearchViewModel.handleFavoriteButtonClick(mockJokeResponseFavorited).observeForever(numberObserver)
        //act
        val liveData = jokeSearchViewModel.handleFavoriteButtonClick(mockJokeResponseFavorited)
        //assert
        assertNotNull(liveData)
        verify(numberObserver).onChanged(loadingResponse())
        verify(numberObserver).onChanged(successfulRemovedFavoritesResponse)
        verifyNoMoreInteractions(numberObserver)
        assertEquals(liveData.blockingObserve(),successfulRemovedFavoritesResponse)
    }
}