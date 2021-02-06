package com.demo.cardinfofinder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
@LargeTest

class MainActivityTest {

     lateinit var ToBetyped: String

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        ToBetyped = "5399 83"
    }

    @Test
    fun checkChangeEditTextWithCardText() {
        // Edit text for .
       onView(withId(R.id.etCardNumber)).perform(typeText(ToBetyped), closeSoftKeyboard())

        onView(withId(R.id.cardNumber))
                .check(matches(withText(ToBetyped)))
    }
}
