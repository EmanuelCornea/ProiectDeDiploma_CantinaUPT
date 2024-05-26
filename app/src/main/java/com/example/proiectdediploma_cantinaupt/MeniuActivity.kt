package com.example.proiectdediploma_cantinaupt

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectdediploma_cantinaupt.databinding.ActivityMeniuBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter


class MeniuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeniuBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val mainCourses: MutableList<Triple<String, String, Double>> = mutableListOf()
    private val sideDishes: MutableList<Triple<String, String, Double>> = mutableListOf()
    private val salads: MutableList<Triple<String, String, Double>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeniuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        loadMenuItems()

        binding.backMenuButton.setOnClickListener {
            finish()
        }

        binding.addFoodButton.setOnClickListener {
            addSelectedMenu()
        }

        binding.qrButtonGenerator.setOnClickListener {
            generateQRCode()
        }
    }

    private fun loadMenuItems() {
        firestore.collection("menu_items").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("name") ?: continue
                    val type = document.getString("type") ?: continue
                    val price = document.getDouble("price") ?: continue

                    Log.d("DEBUG", "Name: $name")
                    Log.d("DEBUG", "Type: $type")
                    Log.d("DEBUG", "Price: $price")

                    val menuItem = Triple(name, type, price)

                    when (type) {
                        "main_course" -> mainCourses.add(menuItem)
                        "side_dish" -> sideDishes.add(menuItem)
                        "salad" -> salads.add(menuItem)
                    }
                }

                binding.mainCourseSpinner.adapter = createAdapter(mainCourses)
                binding.sideDishSpinner.adapter = createAdapter(sideDishes)
                binding.saladSpinner.adapter = createAdapter(salads)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load menu items", Toast.LENGTH_SHORT).show()
            }
    }
    private fun formatItemType(type: String): String {
        return when (type) {
            "main_course" -> "Main Course"
            "side_dish" -> "Side Dish"
            "salad" -> "Salad"
            else -> type
        }
    }

    private fun createAdapter(menuItems: List<Triple<String, String, Double>>): ArrayAdapter<String> {
        val adapterItems = menuItems.map { "${it.first} - ${formatItemType(it.second)} - ${it.third} lei" }
        return ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, adapterItems)
    }


    private fun addSelectedMenu() {
        val mainCourseItem = binding.mainCourseSpinner.selectedItem.toString()
        val (mainCourse, mainCourseType, mainCoursePrice) = parseMenuItem(mainCourseItem)

        val sideDishItem = binding.sideDishSpinner.selectedItem.toString()
        val (sideDish, sideDishType, sideDishPrice) = parseMenuItem(sideDishItem)

        val saladItem = binding.saladSpinner.selectedItem.toString()
        val (salad, saladType, saladPrice) = parseMenuItem(saladItem)

        val currentUser = auth.currentUser
        val email = currentUser?.email

        if (mainCourse.isNotEmpty() && sideDish.isNotEmpty() && salad.isNotEmpty() && email != null) {

            Log.d("DEBUG", "Main Course Price: $mainCoursePrice")
            Log.d("DEBUG", "Side Dish Price: $sideDishPrice")
            Log.d("DEBUG", "Salad Price: $saladPrice")


            val totalPrice = mainCoursePrice + sideDishPrice + saladPrice


            Log.d("DEBUG", "Total Price: $totalPrice")


            val selectedMenu = hashMapOf(
                "email" to email,
                "mainCourse" to mainCourse,
                "mainCourseType" to mainCourseType,
                "mainCoursePrice" to mainCoursePrice,
                "sideDish" to sideDish,
                "sideDishType" to sideDishType,
                "sideDishPrice" to sideDishPrice,
                "salad" to salad,
                "saladType" to saladType,
                "saladPrice" to saladPrice,
                "totalPrice" to totalPrice
            )


            firestore.collection("selected_menus")
                .add(selectedMenu)
                .addOnSuccessListener {
                    Toast.makeText(this, "Menu added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to add menu: $e", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please select a food item from each category and ensure you are logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseMenuItem(selectedItem: String): Triple<String, String, Double> {
        val parts = selectedItem.split(" - ")
        val name = parts.getOrElse(0) { "" }
        val type = parts.getOrElse(1) { "" }
        var price = 0.0


        val priceString = parts.getOrElse(2) { "" }.trim()


        val priceValue = priceString.substringBefore("lei").trim()


        if (priceValue.isNotEmpty()) {
            try {
                price = priceValue.toDouble()
            } catch (e: NumberFormatException) {

                Log.e("ERROR", "Failed to parse price: $priceString")
            }
        }

        return Triple(name, type, price)
    }





    private fun generateQRCode() {
        val textToEncode = "${binding.mainCourseSpinner.selectedItem}\n${binding.sideDishSpinner.selectedItem}\n${binding.saladSpinner.selectedItem}"
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix = qrCodeWriter.encode(textToEncode, BarcodeFormat.QR_CODE, 200, 200)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            binding.QRCodeImage.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}
