package com.example.proiectdediploma_cantinaupt

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInActivityTest {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    @get:Rule
    val activityRule = ActivityTestRule(SignInActivity::class.java)
    @Before
    fun setup() {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth.signOut()
    }
    @Test
    fun testSignInWithValidCredentials() {
        val email = "test12@example.com"
        val password = "parola"

        onView(withId(R.id.email_user)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.pass_user)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())
        Thread.sleep(2000)

        val currentUser = FirebaseAuth.getInstance().currentUser
        assertEquals(email, currentUser?.email)
    }

    @Test
    fun testSignUpWithInvalidEmail() {
        val email ="invalid_email"
        val pass = "password"

        onView(withId(R.id.email_user)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.pass_user)).perform(typeText(pass), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.button_login)).check(matches(isDisplayed()))
    }
    @Test
    fun testSignUpWithShortPassword() {
        onView(withId(R.id.email_user)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.pass_user)).perform(typeText("short"), closeSoftKeyboard())

        onView(withId(R.id.button_login)).perform(click())
        Thread.sleep(3000)

        onView(withId(R.id.button_login)).check(matches(isDisplayed()))
    }
}
