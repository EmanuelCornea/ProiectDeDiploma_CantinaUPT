package com.example.proiectdediploma_cantinaupt

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
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
class SignUpActivityTest {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    @get:Rule
    val activityRule = ActivityTestRule(SignUpActivity::class.java)
    @Before
    fun setup() {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth.signOut()
    }


    @Test
    fun testSignUpWithValidCredentials() {
        val email = "test58d@example.com"
        val password = "parola"

        onView(withId(R.id.email_user)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.pass_user)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.confirm_pass_user)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.role_signup)).perform(click())
        onView(withText("Student")).perform(click())
        onView(withId(R.id.button)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.button_login)).check(matches(isDisplayed()))
    }

    @Test
    fun testSignUpWithInvalidEmail() {
        val email ="invalid_email"
        val pass = "password"
        val confirm_pass= "password"
        onView(withId(R.id.email_user)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.pass_user)).perform(typeText(pass), closeSoftKeyboard())
        onView(withId(R.id.confirm_pass_user)).perform(typeText(confirm_pass), closeSoftKeyboard())
        onView(withId(R.id.role_signup)).perform(click())
        onView(withText("Student")).perform(click())
        onView(withId(R.id.button)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.button_login)).check(doesNotExist())



    }

    @Test
    fun testSignUpWithShortPassword() {
        onView(withId(R.id.email_user)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.pass_user)).perform(typeText("short"), closeSoftKeyboard())
        onView(withId(R.id.confirm_pass_user)).perform(typeText("short"), closeSoftKeyboard())
        onView(withId(R.id.role_signup)).perform(click())
        onView(withText("Student")).perform(click())
        onView(withId(R.id.button)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.button_login)).check(doesNotExist())
    }


    @Test
    fun testSignUpWithSamePassword() {
        onView(withId(R.id.email_user)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.pass_user)).perform(typeText("password1"), closeSoftKeyboard())
        onView(withId(R.id.confirm_pass_user)).perform(typeText("password2"), closeSoftKeyboard())
        onView(withId(R.id.role_signup)).perform(click())
        onView(withText("Student")).perform(click())
        onView(withId(R.id.button)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.button_login)).check(doesNotExist())
    }


}
