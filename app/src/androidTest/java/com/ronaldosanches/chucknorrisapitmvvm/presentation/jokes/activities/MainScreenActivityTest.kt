package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.activities

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.base.BaseMethods
import com.ronaldosanches.chucknorrisapitmvvm.core.di.activitycontext.UseCaseModule
import com.ronaldosanches.chucknorrisapitmvvm.core.di.appcomponent.ApiModule
import com.ronaldosanches.chucknorrisapitmvvm.core.di.appcomponent.AppModule
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.domain.usecases.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(AppModule::class, ApiModule::class, UseCaseModule::class)
@RunWith(AndroidJUnit4::class)
class MainScreenActivityTest : BaseMethods() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var randomJoke: GetRandomJoke
    @Inject lateinit var networkInfo : NetworkInfo

    @Before
    fun setup() {
        hiltRule.inject()
        `when`(networkInfo.result).thenReturn(liveDataNetworkConnected)
    }

    @After
    fun close() {
    }

    @Test
    fun opening_activity_should_load_correct_fragment() {
        val scenario = ActivityScenario.launch(MainScreenActivity::class.java)
        scenario.onActivity {
            val navController = findNavController(it, R.id.nav_host_fragment)
            val currentId : Int? = navController.currentDestination?.id
            assertThat(currentId).isEqualTo(R.id.jokeOptionsFragment)
        }
    }

    @Test
    fun switching_to_dark_mode_should_show_correct_theme_after_restart() {
        //assign creating objects
        val scenario = ActivityScenario.launch(MainScreenActivity::class.java)
        scenario.onActivity {
            val sharedPrefs = it.getSharedPreferences(it.getString(R.string.shared_prefs_file_key),
                Context.MODE_PRIVATE)
            //act adding night mode to shared prefs
            sharedPrefs.edit().putInt(it.getString(R.string.key_pref_night_mode), Constants.NightMode.DARK_MODE)
                .commit()
        }
        //recreating activity
        scenario.recreate()

        scenario.onActivity {
            val sharedPrefs = it.getSharedPreferences(it.getString(R.string.shared_prefs_file_key),
                Context.MODE_PRIVATE)
            val mode = sharedPrefs.getInt(it.getString(R.string.key_pref_night_mode),-1)
            //assert recoved mode has to be correct
            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(mode)
            assertThat(mode).isEqualTo(Constants.NightMode.DARK_MODE)
        }
    }

    @Test
    fun switching_to_light_mode_should_show_correct_theme_after_restart() {
        //assign creating objects
        val scenario = ActivityScenario.launch(MainScreenActivity::class.java)
        scenario.onActivity {
            val sharedPrefs = it.getSharedPreferences(it.getString(R.string.shared_prefs_file_key),
                Context.MODE_PRIVATE)
            //act adding theme to shared prefs
            sharedPrefs.edit().putInt(it.getString(R.string.key_pref_night_mode), Constants.NightMode.LIGHT_MODE)
                .commit()
        }
        //recreating activity
        scenario.recreate()

        scenario.onActivity {
            val sharedPrefs = it.getSharedPreferences(it.getString(R.string.shared_prefs_file_key),
                Context.MODE_PRIVATE)
            val mode = sharedPrefs.getInt(it.getString(R.string.key_pref_night_mode),-1)
            //assert recovered mode has to be correct
            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(mode)
            assertThat(mode).isEqualTo(Constants.NightMode.LIGHT_MODE)
        }
    }

    @Test
    fun on_network_error_should_show_error_alert_and_hide_it_on_interval() = runBlocking {
        `when`(randomJoke.invoke()).thenReturn(errorNetwork())
        ActivityScenario.launch(MainScreenActivity::class.java)
        onView(withId(R.id.tv_main_alert)).check(matches(isDisplayed()))
        delay(Constants.AppConstraints.ALERT_INTERVAL) //want the test to respect this interval
        onView(withId(R.id.tv_main_alert)).check(matches(not(isDisplayed())))
        Unit
    }
}