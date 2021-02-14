package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokecategories

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.base.BaseMethods
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class JokesCategoriesFragmentTest : BaseMethods() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private lateinit var instrumentationContext: Context
    private val navController: NavController = mock(NavController::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun opening_fragment_should_show_list_of_categories() {
        //assign create the arguments for the fragment
        val bundle = Bundle()
        val listSize = categoriesListResponse.size
        bundle.putStringArray("listCategories",mockCategoriesCategories.toTypedArray()) //category "all" is added in the repository
        //act launch the fragment
        launchFragmentInHiltContainer<JokesCategoriesFragment>(bundle)
        //assert check if the list has the same amount of items
        onView(withId(R.id.rv_categories_layout)).check(matches(hasChildCount(listSize)))
    }

    @Test
    fun opening_fragment_should_display_title() {
        //assign create the arguments for the fragment
        val bundle = Bundle()
        bundle.putStringArray("listCategories",mockCategoriesCategories.toTypedArray()) //category "all" is added in the repository
        //act launch the fragment
        launchFragmentInHiltContainer<JokesCategoriesFragment>(bundle)
        //assert check if item has title
        checkIfListHasTextInPosition(position = 0, R.id.rv_categories_layout,
            instrumentationContext.getString(R.string.categories_title))
    }

    @Test
    fun clicking_list_item_should_close_fragment() {
        //assign create the arguments for the fragment
        val bundle = Bundle()
        bundle.putStringArray("listCategories",mockCategoriesCategories.toTypedArray())
        //act launch the fragment
        launchFragmentInHiltContainer<JokesCategoriesFragment>(
            fragmentArgs = bundle, navHostController = navController)
        clickRecyclerViewPosition(1, R.id.rv_categories_layout) //first item is the title
        //assert fragment is closed
        verify(navController).popBackStack()
    }
}