package com.example.proiectdediploma_cantinaupt

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test

class MenuEditActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MenuEditActivity::class.java)

    @Test
    fun testAddMenuItem() {
        val itemName = "Cartofi Naturi"
        val itemType = "side_dish"
        val itemPrice = 12.5

        onView(withId(R.id.newFoodEditText)).perform(typeText(itemName), closeSoftKeyboard())
        onView(withId(R.id.foodTypeSpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(itemType))).perform(click())
        onView(withId(R.id.priceEditText)).perform(typeText(itemPrice.toString()), closeSoftKeyboard())
        onView(withId(R.id.AddFood)).perform(click())


        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("menu_items")
            .whereEqualTo("name", itemName)
            .get()
            .addOnSuccessListener { documents ->
                assert(documents.size() > 0)
            }
            .addOnFailureListener { e ->
                assert(false)
            }
    }






}
