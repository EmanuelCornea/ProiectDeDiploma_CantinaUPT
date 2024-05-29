package com.example.proiectdediploma_cantinaupt

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.firebase.firestore.FirebaseFirestore
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class MeniuActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MeniuActivity::class.java)

    @Test
    fun testMenuSelection() {

        val mainCourse = "Cotlet de Porc"
        val sideDish = "Legume Mexicane"
        val salad = "Salata de rosi"

        // Selecția meniului
        onView(withId(R.id.mainCourseSpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `containsString`(mainCourse))).perform(click())

        onView(withId(R.id.sideDishSpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `containsString`(sideDish))).perform(click())

        onView(withId(R.id.saladSpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `containsString`(salad))).perform(click())


        onView(withId(R.id.addFoodButton)).perform(click())


        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("selected_menus")
            .whereEqualTo("mainCourse", mainCourse)
            .whereEqualTo("sideDish", sideDish)
            .whereEqualTo("salad", salad)
            .get()
            .addOnSuccessListener { documents ->

                assert(documents.size() > 0)
            }
            .addOnFailureListener { e ->

                assert(false)
            }


        Thread.sleep(3000)
    }

    @Test
    fun testQRverification() {


        onView(withId(R.id.qrButtonGenerator)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.QRCodeImage)).check(matches(isDisplayed()))

    }
    }
