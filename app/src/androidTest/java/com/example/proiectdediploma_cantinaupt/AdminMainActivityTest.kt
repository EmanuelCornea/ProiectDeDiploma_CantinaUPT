package com.example.proiectdediploma_cantinaupt

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
class AdminMainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AdminMainActivity::class.java)

    @Test
    fun testUserListButton() {
        Intents.init()
        onView(withId(R.id.UserListButton)).perform(click())
        Intents.intended(hasComponent(UserListActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testMenuEditButton() {
        Intents.init()
        onView(withId(R.id.meniuEditButton)).perform(click())
        Intents.intended(hasComponent(MenuEditActivity::class.java.name))
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
        onView(withId(R.id.profileButton)).perform(click())
        Intents.intended(hasComponent(ProfileActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testOrderListButton() {
        Intents.init()
        onView(withId(R.id.orderListButton)).perform(click())
        Intents.intended(hasComponent(OrderListActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testViewGraphButton() {
        Intents.init()
        onView(withId(R.id.ViewGraphButton)).perform(click())
        Intents.intended(hasComponent(ViewGraphActivity::class.java.name))
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
