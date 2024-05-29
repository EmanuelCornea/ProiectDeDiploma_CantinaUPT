package com.example.proiectdediploma_cantinaupt

import android.widget.Button
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ProfileActivityTest {

    private lateinit var scenario: ActivityScenario<ProfileActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(ProfileActivity::class.java)
    }

    @Test
    fun testProfileEdit() {
        val newName = "John Doe"
        val newAddress = "123 Main St"
        val newPhoneNumber = "555-1234"

        onView(withId(R.id.userName)).perform(replaceText(newName))
        onView(withId(R.id.userAdress)).perform(replaceText(newAddress))
        onView(withId(R.id.userPhoneNumber)).perform(replaceText(newPhoneNumber))

        onView(withId(R.id.saveButton)).perform(click())

       
        onView(withId(R.id.userName)).check(matches(withText(newName)))
        onView(withId(R.id.userAdress)).check(matches(withText(newAddress)))
        onView(withId(R.id.userPhoneNumber)).check(matches(withText(newPhoneNumber)))
    }


}
