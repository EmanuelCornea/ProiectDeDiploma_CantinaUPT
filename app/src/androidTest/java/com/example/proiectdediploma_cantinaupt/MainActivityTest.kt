package com.example.proiectdediploma_cantinaupt

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMeniuButton() {
        Intents.init()
        onView(withId(R.id.meniu_button)).perform(click())
        Intents.intended(hasComponent(MeniuActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testQRButtonEntry() {
        Intents.init()
        onView(withId(R.id.QRbuttonEntry)).perform(click())
        Intents.intended(hasComponent(QRCodeActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testProfileButton() {
        Intents.init()
        onView(withId(R.id.profile_button)).perform(click())
        Intents.intended(hasComponent(ProfileActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testOrderButton() {
        Intents.init()
        onView(withId(R.id.orderButton)).perform(click())
        Intents.intended(hasComponent(OrderActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testLogoutButton() {
        Intents.init()
        onView(withId(R.id.logout)).perform(click())
        Intents.intended(hasComponent(SignInActivity::class.java.name))
        Intents.release()
    }
}
