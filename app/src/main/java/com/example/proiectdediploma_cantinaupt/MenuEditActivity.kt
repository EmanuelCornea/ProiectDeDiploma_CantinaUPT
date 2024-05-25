package com.example.proiectdediploma_cantinaupt

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectdediploma_cantinaupt.databinding.ActivityMenuEditBinding

import com.google.firebase.firestore.FirebaseFirestore


class MenuEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuEditBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        setupFoodTypeSpinner()
        setupListeners()
        loadMenuItems()
    }

    private fun setupFoodTypeSpinner() {
        val foodTypes = listOf("main_course", "side_dish", "salad")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, foodTypes)
        binding.foodTypeSpinner.adapter = adapter
    }

    private fun setupListeners() {
        binding.populateMenuButton.setOnClickListener {
            populateMenuItems()
        }

        binding.AddFood.setOnClickListener {
            val newItem = binding.newFoodEditText.text.toString().trim()
            val foodType = binding.foodTypeSpinner.selectedItem.toString()
            val price = binding.priceEditText.text.toString().toDoubleOrNull()

            if (newItem.isNotEmpty() && foodType.isNotEmpty() && price != null) {
                addMenuItem(newItem, foodType, price)
            } else {
                Toast.makeText(this, "Please enter a food item, select a type, and provide a price", Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteFoodButton.setOnClickListener {
            val selectedItem = binding.spinnerDelete.selectedItem as? String
            selectedItem?.let { deleteMenuItem(it) }
        }
    }


    data class MenuItem(
        val name: String = "",
        val type: String = "",
        val price: Double = 0.0
    )

    private fun populateMenuItems() {
        val initialMenuItems = listOf(
            MenuItem("Piept de Pui", "main_course", 15.0),
            MenuItem("Coaste de Porc", "main_course", 20.0),
            MenuItem("Rasol de Vita", "main_course", 25.0),
            MenuItem("Cartofi Piure", "side_dish", 8.0),
            MenuItem("Cartofi Prajiti", "side_dish", 10.0),
            MenuItem("Cartofi Naturi", "side_dish", 7.0),
            MenuItem("Salata de Varza", "salad", 12.0),
            MenuItem("Salata de Vara", "salad", 10.0),
            MenuItem("Salata Acra", "salad", 9.0)
        )

        for (item in initialMenuItems) {
            val menuItem = hashMapOf(
                "name" to item.name,
                "type" to item.type,
                "price" to item.price
            )
            firestore.collection("menu_items").add(menuItem)
                .addOnSuccessListener {
                    Log.d("MenuEditActivity", "Added item: ${item.name}")
                }
                .addOnFailureListener { e ->
                    Log.e("MenuEditActivity", "Failed to add item: ${item.name}", e)
                }
        }
    }



    private fun addMenuItem(item: String, type: String, price: Double) {
        val menuItem = hashMapOf("name" to item, "type" to type, "price" to price)
        firestore.collection("menu_items")
            .add(menuItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show()
                loadMenuItems()
                binding.newFoodEditText.text.clear()
                binding.priceEditText.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add food item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteMenuItem(item: String) {
        firestore.collection("menu_items").whereEqualTo("name", item).get().addOnSuccessListener { documents ->
            for (document in documents) {
                firestore.collection("menu_items").document(document.id).delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Food item deleted", Toast.LENGTH_SHORT).show()
                        loadMenuItems()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to delete food item", Toast.LENGTH_SHORT).show()
                        Log.e("MenuEditActivity", "Failed to delete item: $item", e)
                    }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to retrieve food item for deletion", Toast.LENGTH_SHORT).show()
            Log.e("MenuEditActivity", "Failed to retrieve item: $item", e)
        }
    }

    private fun loadMenuItems() {
        firestore.collection("menu_items").get().addOnSuccessListener { documents ->
            val menuItems = mutableListOf<String>()
            for (document in documents) {
                document.getString("name")?.let { menuItems.add(it) }
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, menuItems)
            binding.spinnerDelete.adapter = adapter
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to load menu items", Toast.LENGTH_SHORT).show()
            Log.e("MenuEditActivity", "Failed to load menu items", e)
        }
    }
}
