package com.brunofelixdev.mytodoapp.ui.fragment

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.launchFragmentInHiltContainer
import com.brunofelixdev.mytodoapp.ui.activity.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@LargeTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testNavigationToItemFormFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<ItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.fab_add)).perform(click())

        verify(navController).navigate(
            ItemFragmentDirections.navigateToItemForm(null)
        )
    }
}