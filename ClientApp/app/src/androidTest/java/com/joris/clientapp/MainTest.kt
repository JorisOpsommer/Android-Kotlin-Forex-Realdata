package com.joris.clientapp

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.contrib.RecyclerViewActions.scrollTo
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Matcher

@RunWith(AndroidJUnit4::class)
class MainTest {

    @get:Rule
    val actorActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(actorActivityRule.activity.getIdlingResourceInTest())
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(actorActivityRule.activity.getIdlingResourceInTest())
    }

    @Test
    fun testFlow() {
        BaristaSleepInteractions.sleep(5000)
        onView(withId(R.id.recycleviewMostactive)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.txtOneStockCompany)).check(matches(withText(containsString("Company"))))
        onView(withId(R.id.txtOneStockSymbol)).check(matches(withText(containsString("Symbol"))))
        onView(withId(R.id.txtOneStockPrice)).check(matches(withText(containsString("Price"))))
        onView(withId(R.id.txtOneStockHigh)).check(matches(withText(containsString("High"))))
        onView(withId(R.id.txtOneStockLow)).check(matches(withText(containsString("Low"))))
        onView(withId(R.id.txtOneStockVolume)).check(matches(withText(containsString("Volume"))))
        onView(withId(R.id.txtOneStockAvgVolume)).check(matches(withText(containsString("Avg"))))
        onView(withId(R.id.txtOneStockExchange)).check(matches(withText(containsString("Exchange"))))
    }

    @Test
    fun testSearchSpecificStock() {
        onView(withId(R.id.navigation_search)).perform(click())
        onView(withId(R.id.txtSearchStock)).perform(typeText("tsl"))

        closeSoftKeyboard()
        BaristaSleepInteractions.sleep(5000)
        onView(withId(R.id.recyclerviewNameList))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(2,click()))
        onView(withId(R.id.txtOneStockSymbolFill)).check(matches(withText(containsString("MTSL"))))

    }

    @Test
fun testNavigationbar(){
        onView(withId(R.id.navigation_search)).perform(click())
        onView(withId(R.id.txtSearchStock)).perform(typeText("apple"))
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.navigation_search)).perform(click())
        onView(withId(R.id.txtSearchStock)).check(matches(withText(containsString("apple"))))
        onView(withId(R.id.navigation_history)).perform(click())
        onView(withId(R.id.txtHistoryWelcome)).check(matches(withText(containsString("Search history"))))


        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.txtWelcome)).check(matches(withText(containsString("STOCKTRADING"))))

    }


}