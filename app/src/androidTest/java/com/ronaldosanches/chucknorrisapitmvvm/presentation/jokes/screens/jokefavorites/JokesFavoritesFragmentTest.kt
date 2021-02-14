package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokefavorites

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.base.BaseMethods
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.launchFragmentInHiltContainer
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.GetAllFavoriteJokes
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.RemoveJokeFromFavorites
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class JokesFavoritesFragmentTest : BaseMethods() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private lateinit var instrumentationContext: Context
    @Inject lateinit var allFavoriesList : GetAllFavoriteJokes
    @Inject lateinit var removeJokeFromFavorites: RemoveJokeFromFavorites

    @Before
    fun setup() {
        hiltRule.inject()
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun with_input_favorites_favorite_list_should_be_displayed() {
        //assign create the mock to display list and list size used
        `when`(allFavoriesList()).thenReturn(successfulFavoriteListResponse)
        val listSize = listFavoritedJokes.size + 1 //list size + title

        //act open fragment
        launchFragmentInHiltContainer<JokesFavoritesFragment>()

        //assert list displayed has the correct size
        onView(withId(R.id.rv_favorite_jokes_list))
            .check(ViewAssertions.matches(ViewMatchers.hasChildCount(listSize)))
    }

    @Test
    fun opening_fragment_should_display_title() {
        //assign create the mock to display list
        `when`(allFavoriesList()).thenReturn(successfulFavoriteListResponse)

        //act open fragment
        launchFragmentInHiltContainer<JokesFavoritesFragment>()

        //assert check if item has title
        checkIfListHasTextInPosition(position = 0, R.id.rv_favorite_jokes_list,
            instrumentationContext.getString(R.string.favorites_title))
    }
}