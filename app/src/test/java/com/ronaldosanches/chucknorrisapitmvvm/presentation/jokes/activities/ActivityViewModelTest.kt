package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.activities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.ronaldosanches.chucknorrisapitmvvm.CoroutineTestRule
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewmodels.ViewModelBaseTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActivityViewModelTest : ViewModelBaseTest() {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var unitObserver : Observer<Unit>
    private lateinit var stringObserver : Observer<Int>

    @Before
    fun setup() {
        unitObserver = mock()
        stringObserver = mock()
        activityViewModel = ActivityViewModel()
    }

    @Test
    fun `on handleBackPressed valid popBack should emit empty function field`() {
        //arrange
        val popBack = false
        activityViewModel.handleBackPressed(popBack).observeForever(unitObserver)
        //act
        val liveData = activityViewModel.handleBackPressed(popBack)
        //assert
        assertNotNull(liveData)
        verify(unitObserver, times(1)).onChanged(Unit)
    }

    @Test
    fun `on handleBackPressed invalid popBack should do nothing`() {
        //arrange
        val popBack = null
        activityViewModel.handleBackPressed(popBack).observeForever(unitObserver)
        //act
        val liveData = activityViewModel.handleBackPressed(popBack)
        //assert
        assertNotNull(liveData)
        verifyZeroInteractions(unitObserver)
    }

    @Test
    fun `using night mode on getCurrentNext theme should return light mode`() {
        //arrange
        val mode = Constants.NightMode.DARK_MODE
        activityViewModel.getCurrentNextTheme(mode).observeForever(stringObserver)
        //act
        val liveData = activityViewModel.getCurrentNextTheme(mode)
        //assert
        assertNotNull(liveData)
        verify(stringObserver).onChanged(Constants.NightMode.LIGHT_MODE)
    }

    @Test
    fun `using light mode on getCurrentNext theme should return dark mode`() {
        //arrange
        val mode = Constants.NightMode.LIGHT_MODE
        activityViewModel.getCurrentNextTheme(mode).observeForever(stringObserver)
        //act
        val liveData = activityViewModel.getCurrentNextTheme(mode)
        //assert
        assertNotNull(liveData)
        verify(stringObserver).onChanged(Constants.NightMode.DARK_MODE)
    }
}