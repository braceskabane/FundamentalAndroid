package com.dicoding.restaurantreview

import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.restaurantreview.ui.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class DetaiProfileUITest {

    private val jumlahFollowing = "0"
    private val jumlahFollower = "0"

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testSearchUserAndNavigateToDetail() {
        Thread.sleep(2000)
        onView(withId(R.id.searchBar)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("B"), closeSoftKeyboard())
        Thread.sleep(3000)
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("r"), closeSoftKeyboard())
        Thread.sleep(3000)
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("a"), closeSoftKeyboard())
        Thread.sleep(3000)
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("c"), closeSoftKeyboard())
        Thread.sleep(3000)
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("e"), closeSoftKeyboard())
        Thread.sleep(3000)
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("s"), closeSoftKeyboard())
        Thread.sleep(3000)
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("k"), closeSoftKeyboard())
        Thread.sleep(5000)
        onView(withId(R.id.avatarCardView)).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.text_followers_count)).check(matches(withText(jumlahFollower)))
        onView(withId(R.id.text_following_count)).check(matches(withText(jumlahFollowing)))
    }
}
