@file:Suppress("SameParameterValue")

package com.ronaldosanches.chucknorrisapitmvvm.core.base

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.ronaldosanches.chucknorrisapitmvvm.ObjectResources
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.RecyclerViewMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

open class BaseMethods : ObjectResources() {

    companion object {
        const val FIRST_POSITION = 0
    }

    protected fun findNavController(activity: Activity, @IdRes navHostId: Int) = activity.findNavController(
        navHostId
    )

    protected fun clickButton(@IdRes resId: Int): ViewInteraction = onView((withId(resId))).perform(click())

    protected fun checkIfTextIsDisplayedInView(viewId: Int, text: String) {
        onView(withId(viewId)).check(matches(withText(text)))
    }

    protected fun clickRecyclerViewPosition(position: Int = FIRST_POSITION, @IdRes recyclerViewId: Int): ViewInteraction =
        onView(withRecyclerView(recyclerViewId)?.atPosition(position)).perform(click())

    protected fun clickItemInsideRecyclerView(position: Int = FIRST_POSITION, @IdRes recyclerViewId: Int,
                                              @IdRes itemId : Int): ViewInteraction = onView(withId(recyclerViewId))
        .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position,clickChildViewWithId(itemId)))

    protected fun checkIfListHasTextInPosition(
        position: Int = FIRST_POSITION,
        @IdRes resId: Int,
        text: String
    ): ViewInteraction = onView(withRecyclerView(resId)?.atPosition(position)).check(matches(withText(text)))

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child view in position $position in its father")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent)
                    && view == parent.getChildAt(position)
        }
    }

    open fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher? {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View?) {
                val v = view?.findViewById<View>(id)
                v?.performClick() ?: click()
            }
        }
    }

}