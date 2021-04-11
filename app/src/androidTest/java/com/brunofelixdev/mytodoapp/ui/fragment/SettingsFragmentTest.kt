package com.brunofelixdev.mytodoapp.ui.fragment

import android.content.Context
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.pref.getCurrentThemeMode
import com.brunofelixdev.mytodoapp.ui.activity.MainActivity
import com.brunofelixdev.mytodoapp.util.Constants
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class SettingsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    lateinit var appContext: Context
    lateinit var fragment: FragmentScenario<SettingsFragment>

    @Before
    fun setUp() {
        hiltRule.inject()
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        fragment = launchFragmentInContainer<SettingsFragment>()
    }

    @Test
    fun testIsTxtThemeTitleIsVisible() {
        onView(withId(R.id.tv_theme)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_theme)).check(matches(withText(R.string.label_theme_settings)))
    }

    @Test
    fun testIsTxtThemeDescriptionIsVisible() {
        onView(withId(R.id.tv_theme_description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_theme_description)).check(matches(withText(R.string.txt_theme_settings)))
    }

    @Test
    fun testIsRadioGroupIsVisible() {
        onView(withId(R.id.rg_parent)).check(matches(isDisplayed()))
    }

    @Test
    fun testIsNightThemeIsDisplayed() {
        onView(withId(R.id.radio_night)).perform(click())

        assertThat(getCurrentThemeMode(appContext)).isEqualTo(Constants.PREF_NIGHT)
    }

    @Test
    fun testIsLightThemeIsDisplayed() {
        onView(withId(R.id.radio_light)).perform(click())

        assertThat(getCurrentThemeMode(appContext)).isEqualTo(Constants.PREF_LIGHT)
    }
}