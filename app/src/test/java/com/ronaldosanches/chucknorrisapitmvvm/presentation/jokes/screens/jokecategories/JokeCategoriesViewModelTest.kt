package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokecategories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.ronaldosanches.chucknorrisapitmvvm.CoroutineTestRule
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.GenericListItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewmodels.ViewModelBaseTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

class JokeCategoriesViewModelTest: ViewModelBaseTest() {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var jokeCategoriesViewModel: JokeCategoriesViewModel
    private lateinit var viewTypeObserver: Observer<List<ViewType>>

    @Before
    fun setup() {
        viewTypeObserver = mock()
//        jokeCategoriesViewModel = JokeCategoriesViewModel()
    }

//    @Test
//    fun `adding category menu should return viewtype menu with title`() {
//        //arrange
//        jokeCategoriesViewModel.createCategoriesMenu(mockCategoriesCategories.toTypedArray())
//            .observeForever(viewTypeObserver)
//        //act
//        val liveData = jokeCategoriesViewModel.createCategoriesMenu(mockCategoriesCategories
//            .toTypedArray()).blockingObserve()
//        //assert
//        assertNotNull(liveData)
//        verify(viewTypeObserver).onChanged(ArgumentMatchers.anyList())
//        assertTrue(liveData is List<ViewType>)
//        assertTrue(liveData?.first() is SectionTitleItem)
//        assertTrue(liveData?.last() is GenericListItem)
//    }
}