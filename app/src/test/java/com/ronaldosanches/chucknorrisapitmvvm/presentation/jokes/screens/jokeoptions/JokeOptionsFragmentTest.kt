package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokeoptions

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.nhaarman.mockitokotlin2.*
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryType
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.*
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewmodels.ViewModelBaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers


@RunWith(JUnit4::class)
class JokeOptionsViewModelTest : ViewModelBaseTest() {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCaseGetRandomJoke: GetRandomJoke
    private lateinit var useCaseGetRandomJokeByCategory: GetRandomJokeByCategory
    private lateinit var useCaseGetCategory: GetCategories
    private lateinit var useCaseAddJokeToFavorites: AddJokeToFavorites
    private lateinit var useCaseJokeIsFavorited: CheckIfJokeIsFavorited
    private lateinit var useCaseRemoveJokeFromFavorites: RemoveJokeFromFavorites
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var jokesViewModel: JokeOptionsViewModel
    private lateinit var randomJokeObserver : Observer<in ResultChuck<out JokeResponse>>
    private lateinit var jokeBySearchObserver : Observer<in ResultChuck<out SearchResponse>>
    private lateinit var categoriesObserver : Observer<in ResultChuck<out List<String>>>
    private lateinit var numberObserver : Observer<ResultChuck<out Number>>
    private lateinit var menuOptionsObserver : Observer<List<Int>>
    private lateinit var searchQueryObserver : Observer<Boolean>
    private lateinit var nullableStringObserver : Observer<String?>
    private lateinit var categoryTypeAll: CategoryType
    private lateinit var categoryTypeCategory: CategoryType
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        useCaseGetRandomJoke = mock()
        useCaseGetRandomJokeByCategory = mock()
        useCaseGetCategory = mock()
        useCaseAddJokeToFavorites = mock()
        useCaseJokeIsFavorited = mock()
        useCaseRemoveJokeFromFavorites = mock()
        savedStateHandle = mock()
        randomJokeObserver = mock()
        jokeBySearchObserver = mock()
        categoriesObserver = mock()
        numberObserver = mock()
        menuOptionsObserver = mock()
        searchQueryObserver = mock()
        nullableStringObserver = mock()
        categoryTypeAll = CategoryType.CategoryAll
        categoryTypeCategory = CategoryType.CategoryFromApi(mockCategoriesCategory)
        jokesViewModel = spy(
            JokeOptionsViewModel(
                useCaseGetRandomJoke,
                useCaseGetRandomJokeByCategory,
                useCaseGetCategory,
                useCaseAddJokeToFavorites,
                useCaseJokeIsFavorited,
                useCaseRemoveJokeFromFavorites,
                savedStateHandle)
        )
        jokesViewModel.coroutineContext = testScope.coroutineContext
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `using state saved true on getJokeByCategory should return valid response`() = testDispatcher.runBlockingTest {
        //arrange
        whenever(savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)).thenReturn(mockJokeResponse)
        jokesViewModel.getRandomJokeByCategory(true).observeForever(randomJokeObserver)
        //act
        val liveData = jokesViewModel.getRandomJokeByCategory(true)
        //assert
        assertNotNull(liveData)
        verify(randomJokeObserver).onChanged(successfulJokeResponse)
        verify(useCaseGetRandomJoke, atMost(0)).invoke()
        verifyNoMoreInteractions(randomJokeObserver)
    }

    @Test
    fun `using category all on getJokeByCategory should return valid response`() = testDispatcher.runBlockingTest {
        //arrange
        whenever(useCaseGetRandomJoke()).thenReturn(successfulJokeResponse)
        whenever(savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)).thenReturn(mockCategoriesCategoryAll)
        jokesViewModel.getRandomJokeByCategory(false).observeForever(randomJokeObserver)
        //act
        val liveData = jokesViewModel.getRandomJokeByCategory(false)
        //assert
        assertNotNull(liveData)
        verify(useCaseGetRandomJoke).invoke()
        verify(randomJokeObserver).onChanged(loadingResponse())
        verify(randomJokeObserver).onChanged(successfulJokeResponse)
        verifyNoMoreInteractions(randomJokeObserver)
    }

    @Test
    fun `using specific category on getJokeByCategory should return valid response`() = testDispatcher.runBlockingTest {
        //arrange
        whenever(useCaseGetRandomJokeByCategory(mockCategoriesCategory)).thenReturn(successfulJokeResponse)
        whenever(savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)).thenReturn(mockCategoriesCategory)
        jokesViewModel.getRandomJokeByCategory(false).observeForever(randomJokeObserver)
        //act
        val liveData = jokesViewModel.getRandomJokeByCategory(false)
        //assert
        assertNotNull(liveData)
        verify(useCaseGetRandomJokeByCategory).invoke(mockCategoriesCategory)
        verify(randomJokeObserver).onChanged(loadingResponse())
        verify(randomJokeObserver).onChanged(successfulJokeResponse)
        verifyNoMoreInteractions(randomJokeObserver)
    }

    @Test
    fun `get error network from all category should error response`() = testDispatcher.runBlockingTest {
        //arrange
        whenever(useCaseGetRandomJoke()).thenReturn(errorNetwork())
        whenever(savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)).thenReturn(mockCategoriesCategoryAll)
        jokesViewModel.getRandomJokeByCategory(false).observeForever(randomJokeObserver)
        //act
        val liveData = jokesViewModel.getRandomJokeByCategory(false)
        //assert
        assertNotNull(liveData)
        verify(useCaseGetRandomJoke).invoke()
        verify(randomJokeObserver).onChanged(loadingResponse())
        verify(randomJokeObserver).onChanged(errorNetwork())
        verifyNoMoreInteractions(randomJokeObserver)
    }

    @Test
    fun `get unknown error from all category should error response`() = testDispatcher.runBlockingTest {
        //arrange
        whenever(useCaseGetRandomJoke()).thenReturn(errorUnknown())
        whenever(savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)).thenReturn(mockCategoriesCategoryAll)
        jokesViewModel.getRandomJokeByCategory(false).observeForever(randomJokeObserver)
        //act
        val liveData = jokesViewModel.getRandomJokeByCategory(false)
        //assert
        assertNotNull(liveData)
        verify(useCaseGetRandomJoke).invoke()
        verify(randomJokeObserver).onChanged(loadingResponse())
        verify(randomJokeObserver).onChanged(errorUnknown())
        verifyNoMoreInteractions(randomJokeObserver)
    }

    @Test
    fun `get cache error from all category should error response`() = testDispatcher.runBlockingTest {
        //arrange
        whenever(useCaseGetRandomJoke()).thenReturn(errorCacheNotFound())
        whenever(savedStateHandle.get<String>(KEY_LAST_CATEGORY_PICKED)).thenReturn(mockCategoriesCategoryAll)
        jokesViewModel.getRandomJokeByCategory(false).observeForever(randomJokeObserver)
        //act
        val liveData = jokesViewModel.getRandomJokeByCategory(false)
        //assert
        assertNotNull(liveData)
        verify(useCaseGetRandomJoke).invoke()
        verify(randomJokeObserver).onChanged(loadingResponse())
        verify(randomJokeObserver).onChanged(errorCacheNotFound())
        verifyNoMoreInteractions(randomJokeObserver)
    }

    @Test
    fun `get categories should return successful response`() = testDispatcher.runBlockingTest {
        //arrange
        whenever(useCaseGetCategory()).thenReturn(successfulCategoriesList)
        jokesViewModel.getCategoriesFromApi().observeForever(categoriesObserver)
        //act
        val liveData = jokesViewModel.getCategoriesFromApi()
        //assert
        assertNotNull(liveData)
        verify(useCaseGetCategory).invoke()
        verify(categoriesObserver).onChanged(loadingResponse())
        verify(categoriesObserver).onChanged(successfulCategoriesList)
        verifyNoMoreInteractions(categoriesObserver)
    }

    @Test
    fun `check if joke is favorite given id should return boolean`() = testDispatcher.runBlockingTest {
        //arrange
        whenever(useCaseJokeIsFavorited(mockJokeResponseId)).thenReturn(successfulCheckIfJokeIsFavoritedResponse)
        whenever(savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)).thenReturn(mockJokeResponse)
        //act
        val result = jokesViewModel.checkIfJokeIsFavorited()
        //assert
        assertNotNull(result)
        verify(useCaseJokeIsFavorited).invoke(mockJokeResponseId)
        verify(jokesViewModel).updateFavoriteState(checkIfFavoritedPositiveResponse) //spy
    }


    @Test
    fun `when favorite is false handleFavorite should add joke to favorites and return Long`() = testDispatcher.runBlockingTest {
        //assign
        whenever(useCaseAddJokeToFavorites.invoke(mockJokeResponse)).thenReturn(successfulAddedFavoritesResponse)
        whenever(savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)).thenReturn(mockJokeResponse)
        jokesViewModel.handleFavoriteButtonClick().observeForever(numberObserver)
        //act
        val liveData = jokesViewModel.handleFavoriteButtonClick()
        //assert
        assertNotNull(liveData)
        verify(numberObserver).onChanged(loadingResponse())
        verify(numberObserver).onChanged(ResultChuck.Success(idJokeResponseAddedItem))
        verify(useCaseAddJokeToFavorites).invoke(mockJokeResponse)
        verify(useCaseRemoveJokeFromFavorites, times(0)).invoke(any())
    }

    @Test
    fun `when favorite is true handleFavorite should remove joke to favorites and return Int`() = testDispatcher.runBlockingTest {
        //assign
        val newMockJokeResponse = JokeResponse(jokeResponse = mockJokeResponse, isFavorite = true)
        whenever(useCaseRemoveJokeFromFavorites.invoke(newMockJokeResponse)).thenReturn(successfulRemovedFavoritesResponse)
        whenever(savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)).thenReturn(newMockJokeResponse)
        jokesViewModel.handleFavoriteButtonClick().observeForever(numberObserver)
        //act
        val liveData = jokesViewModel.handleFavoriteButtonClick()
        //assert
        assertNotNull(liveData)
        verify(numberObserver).onChanged(loadingResponse())
        verify(numberObserver).onChanged(ResultChuck.Success(amountItemsRemovedJokeResponseDb))
        verify(useCaseRemoveJokeFromFavorites).invoke(newMockJokeResponse)
        verify(useCaseAddJokeToFavorites, times(0)).invoke(any())
    }

    @Test
    fun `click favorite without joke previously loaded should return Error Object Not Found`() = testDispatcher.runBlockingTest {
        //assign
        whenever(savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)).thenReturn(null)
        jokesViewModel.handleFavoriteButtonClick().observeForever(numberObserver)
        //act
        val liveData = jokesViewModel.handleFavoriteButtonClick()
        //assert
        assertNotNull(liveData)
        verify(numberObserver).onChanged(loadingResponse())
        verify(numberObserver).onChanged(errorNotFoundFavoriteResponse)
        verify(useCaseRemoveJokeFromFavorites, times(0)).invoke(any())
        verify(useCaseAddJokeToFavorites, times(0)).invoke(any())
    }

    @Test
    fun `create menu options should load list of Int`() {
        //assign
        jokesViewModel.createMenuOptions().observeForever(menuOptionsObserver)
        //act
        val liveData = jokesViewModel.createMenuOptions().blockingObserve()
        //assert
        assertNotNull(liveData)
        verify(menuOptionsObserver).onChanged(ArgumentMatchers.anyList())
        verifyNoMoreInteractions(menuOptionsObserver)
        assertTrue(liveData is List<Int>)
    }

    @Test
    fun `using invalid search query size should return false`() {
        //assign
        val invalidQuerySize = 0
        jokesViewModel.isSearchQueryValid(invalidQuerySize).observeForever(searchQueryObserver)
        //act
        val liveData = jokesViewModel.isSearchQueryValid(invalidQuerySize)
        //assert
        assertNotNull(liveData)
        verify(searchQueryObserver).onChanged(false)
    }

    @Test
    fun `using valid search query size should return true`() {
        //assign
        val invalidQuerySize = 32
        jokesViewModel.isSearchQueryValid(invalidQuerySize).observeForever(searchQueryObserver)
        //act
        val liveData = jokesViewModel.isSearchQueryValid(invalidQuerySize)
        //assert
        assertNotNull(liveData)
        verify(searchQueryObserver).onChanged(true)
    }

    @Test
    fun `cheking category result valid should return valid valid result`() {
        //assign
        val bundle : Bundle = mock()
        val requestKey = KEY_SELECTED_CATEGORY
        whenever(bundle.containsKey(KEY_SELECTED_CATEGORY_STRING)).thenReturn(true)
        whenever(bundle.getString(KEY_SELECTED_CATEGORY_STRING)).thenReturn(mockCategoriesCategory)
        jokesViewModel.handleCategoryResult(requestKey, bundle).observeForever(nullableStringObserver)
        //act
        val liveData = jokesViewModel.handleCategoryResult(requestKey,bundle)
        //assert
        assertNotNull(liveData)
        verify(nullableStringObserver).onChanged(mockCategoriesCategory)
    }

    @Test
    fun `when sharing joke value is valid should share`() {
        //assign
        whenever(savedStateHandle.get<JokeResponse>(KEY_LAST_JOKE)).thenReturn(mockJokeResponse)
        jokesViewModel.shareJoke().observeForever(nullableStringObserver)
        //act
        val liveData = jokesViewModel.shareJoke()
        //assert
        assertNotNull(liveData)
        verify(nullableStringObserver).onChanged(mockJokeResponseJoke)
        verifyNoMoreInteractions(nullableStringObserver)
    }
}