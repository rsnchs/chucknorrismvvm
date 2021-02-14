package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokeoptions

import android.content.Context
import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants.HttpParameters.MIMETYPE_TEXT
import com.ronaldosanches.chucknorrisapitmvvm.core.base.BaseMethods
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.launchFragmentInHiltContainer
import com.ronaldosanches.chucknorrisapitmvvm.core.di.activitycontext.UseCaseModule
import com.ronaldosanches.chucknorrisapitmvvm.core.di.appcomponent.AppModule
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(UseCaseModule::class, AppModule::class)
@RunWith(AndroidJUnit4::class)
class JokeOptionsFragmentTest : BaseMethods() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var instrumentationContext: Context
    @Inject lateinit var randomJoke: GetRandomJoke
    @Inject lateinit var randomJokeByCategory: GetRandomJokeByCategory
    @Inject lateinit var getCategories: GetCategories
    @Inject lateinit var addJokesToFavorites: AddJokeToFavorites
    @Inject lateinit var removeJokeFromFavorites: RemoveJokeFromFavorites
    @Inject lateinit var networkInfo : NetworkInfo
    private val navController: NavController = mock(NavController::class.java)
    private val navDestination: NavDestination = mock(NavDestination::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
        instrumentationContext = getInstrumentation().targetContext
    }

    @Test
    fun opening_screen_should_display_title() = runBlockingTest {
        //assign mocks to open the screen
        `when`(randomJoke.invoke()).thenReturn(successfulJokeResponse)
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
        //act launch fragment
        launchFragmentInHiltContainer<JokeOptionsFragment>()
        //assert text title is visible
        checkIfTextIsDisplayedInView(R.id.tv_joke_options_title,
            instrumentationContext.getString(R.string.app_title))
    }

    @Test
    fun use_is_offline_should_display_correct_icon()  {
        //assign mocks to open screeen
        `when`(networkInfo.result).thenReturn(liveDataNetworkDisconnected)
        //act open fragment
        launchFragmentInHiltContainer<JokeOptionsFragment>()
        //assert icon show user is offline
        onView(withId(R.id.bt_top_menu_connection)).check(ViewAssertions.matches(not(isSelected())))
        verify(networkInfo).result
    }

    @Test
    fun clicking_categories_should_trigger_method_to_open_categories() = runBlockingTest {
        //assign expected api response from categories
        val responseList = categoriesListResponse.toTypedArray()
        `when`(getCategories.invoke()).thenReturn(successfulCategoriesList)
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
        `when`(navController.currentDestination).thenReturn(navDestination)
        `when`(navDestination.id).thenReturn(R.id.jokeOptionsFragment)
        //act launch fragment and click first item position
        launchFragmentInHiltContainer<JokeOptionsFragment>(navHostController = navController)
        clickRecyclerViewPosition(position = 0, recyclerViewId = R.id.rv_joke_options_options_list)
        //assert trigger method that loads JokeCategoryFragment
        verify(navController).currentDestination
        verify(navController).navigate(
            refEq(JokeOptionsFragmentDirections
                .actionJokeOptionsFragmentToJokesCategoriesFragment(responseList))
        )
    }

    @Test
    fun clicking_all_favorite_jokes_should_trigger_method_to_open_favorite_jokes() {
        //assign
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
        `when`(navController.currentDestination).thenReturn(navDestination)
        `when`(navDestination.id).thenReturn(R.id.jokeOptionsFragment)
        //act launch fragment and click first item position
        launchFragmentInHiltContainer<JokeOptionsFragment>(navHostController = navController)
        clickRecyclerViewPosition(position = 1, recyclerViewId = R.id.rv_joke_options_options_list)
        //assert trigger method that loads JokeFavoritesFragment
        verify(navController).currentDestination
        verify(navController).navigate((JokeOptionsFragmentDirections
            .actionJokeOptionsFragmentToJokesListFragment()))
    }

    @Test
    fun using_search_should_trigger_method_to_open_search_results() {
        //assign search query
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
        `when`(navController.currentDestination).thenReturn(navDestination)
        `when`(navDestination.id).thenReturn(R.id.jokeOptionsFragment)
        val searchQuery = searchQuery
        //act type text in edittext and search
        launchFragmentInHiltContainer<JokeOptionsFragment>(navHostController = navController)
        onView(allOf(supportsInputMethods(), isDescendantOfA(withId(R.id.tl_joke_options_search_layout))))
            .perform(typeText(searchQuery))
        onView(withContentDescription(instrumentationContext.getString(R.string.content_description_search_icon)))
            .perform(click())

        //assert trigger method that loads JokeSearchFragment
        verify(navController).currentDestination
        verify(navController).navigate(JokeOptionsFragmentDirections
            .actionJokeOptionsFragmentToJokeSearchFragment(searchQuery))
    }

    @Test
    fun clicking_share_button_should_start_action_intent() = runBlockingTest {
        //assign required mocks to display screen and init Intent monitoring
        Intents.init()
        `when`(randomJoke.invoke()).thenReturn(successfulJokeResponse)
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
        //act launch fragment and click share button
        launchFragmentInHiltContainer<JokeOptionsFragment>()
        clickButton(R.id.bt_joke_content_share)
        //assert intent is of type send and has data
        intended(hasAction(Intent.ACTION_SEND))
        intended(hasType(MIMETYPE_TEXT))
        Intents.release()
    }

    @Test
    fun clicking_favorite_button_should_trigger_add_to_favorite() = runBlockingTest {
        //assign required mocks
        `when`(randomJoke.invoke()).thenReturn(successfulJokeResponse)
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
        //act launch fragment and click favorite button
        launchFragmentInHiltContainer<JokeOptionsFragment>()
        clickButton(R.id.bt_joke_content_favorite)
        //assert addToFavorite was clicked
        verify(addJokesToFavorites).invoke(mockJokeResponse)
        verifyZeroInteractions(removeJokeFromFavorites)
    }

    @Test
    fun clicking_favorite_button_should_trigger_remove_from_favorites() = runBlockingTest {
        //assign required mocks
        `when`(randomJoke.invoke()).thenReturn(successfulJokeResponseFavorited)
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
        //act launch fragment and click favorite button
        launchFragmentInHiltContainer<JokeOptionsFragment>()
        clickButton(R.id.bt_joke_content_favorite)
        //assert addToFavorite was clicked
        verify(removeJokeFromFavorites).invoke(mockJokeResponseFavorited)
        verifyZeroInteractions(addJokesToFavorites)
    }

    @Test
    fun clicking_load_another_from_all_category_should_trigger_method_to_load_joke() = runBlockingTest {
        //assign required mocks
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
        `when`(randomJoke.invoke()).thenReturn(successfulJokeResponse)
        //act launch fragment and click load another button
        launchFragmentInHiltContainer<JokeOptionsFragment>()
        clickButton(R.id.bt_random_joke_load_more)
        verify(randomJoke, times(2)).invoke()
        verifyZeroInteractions(randomJokeByCategory)
    }
}