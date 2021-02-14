package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokesearch

import android.content.Context
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.base.BaseMethods
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.launchFragmentInHiltContainer
import com.ronaldosanches.chucknorrisapitmvvm.core.di.activitycontext.UseCaseModule
import com.ronaldosanches.chucknorrisapitmvvm.core.di.appcomponent.AppModule
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetJokeBySearch
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(UseCaseModule::class, AppModule::class)
@RunWith(AndroidJUnit4::class)
class JokeSearchFragmentTest : BaseMethods() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private lateinit var instrumentationContext: Context
    @Inject lateinit var getJokeBySearch: GetJokeBySearch


    @Before
    fun setup() {
        hiltRule.inject()
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun on_search_response_should_display_items() = runBlockingTest {
        //assign create the mock to display list
        `when`(getJokeBySearch(searchQuery)).thenReturn(successfulSearchResponse)
        val listSize = mockSearchResponseJokes.size + 1 //list + title
        val bundle = Bundle()
        bundle.putString("query", searchQuery)
        //act open fragment
        launchFragmentInHiltContainer<JokeSearchFragment>(bundle)

        //assert list size
        onView(withId(R.id.rv_joke_search_list)).check(matches(hasChildCount(listSize)))
    }

    @Test
    fun opening_fragment_should_display_title() = runBlockingTest {
        //assign create the mock to display list
        `when`(getJokeBySearch(searchQuery)).thenReturn(successfulSearchResponse)
        val bundle = Bundle()
        bundle.putString("query", searchQuery)
        //act open fragment
        launchFragmentInHiltContainer<JokeSearchFragment>(bundle)

        //assert check if item has title
        checkIfListHasTextInPosition(
            position = 0, R.id.rv_joke_search_list,
            instrumentationContext.getString(R.string.search_title)
        )
    }

    @Test
    fun on_empty_should_response_should_show_empty_response_message() = runBlockingTest {
        //assign create the mock to display list
        `when`(getJokeBySearch(searchQuery)).thenReturn(successfulEmptySearchResponse)
        val bundle = Bundle()
        bundle.putString("query", searchQuery)
        //act open fragment
        launchFragmentInHiltContainer<JokeSearchFragment>(bundle)

        //assert list size
        onView(withId(R.id.rv_joke_search_list)).check(matches(hasChildCount(1)))
    }
}