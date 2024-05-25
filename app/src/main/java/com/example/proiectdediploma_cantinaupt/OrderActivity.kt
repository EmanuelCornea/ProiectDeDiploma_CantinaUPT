package com.example.proiectdediploma_cantinaupt

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat

class OrderActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var listView: ListView
    private lateinit var ordersList: MutableList<String>
    private lateinit var ordersAdapter: ArrayAdapter<String>
    private val decimalFormat = DecimalFormat("0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        listView = findViewById(R.id.listOrder)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        ordersList = mutableListOf()
        ordersAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ordersList)
        listView.adapter = ordersAdapter

        fetchOrders()

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val order = ordersList[position]
            showDeleteConfirmationDialog(order, position)
            true
        }
    }
    override fun onBackPressed() {

        super.onBackPressed()
    }
    private fun fetchOrders() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                firestore.collection("selected_menus")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { result ->
                        ordersList.clear()

                        for (document in result) {
                            val order = document.toObject(MeniuAles2::class.java)
                            if (order != null) {
                                val orderText = "${order.mainCourse ?: ""} (${formatPrice(order.mainCoursePrice)} lei), " +
                                        "${order.sideDish ?: ""} (${formatPrice(order.sideDishPrice)} lei), " +
                                        "${order.salad ?: ""} (${formatPrice(order.saladPrice)} lei), " +
                                        "Total: ${formatPrice(order.totalPrice)} lei"
                                ordersList.add(orderText)
                            }
                        }

                        ordersAdapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this@OrderActivity, "Failed to load orders: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this@OrderActivity, "No email associated with the current user.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@OrderActivity, "No user is currently logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmationDialog(order: String, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Order")
            .setMessage("Are you sure you want to delete this order?")
            .setPositiveButton("Yes") { dialog, which ->
                deleteOrder(order, position)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteOrder(order: String, position: Int) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                firestore.collection("selected_menus")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val orderInDb = document.toObject(MeniuAles2::class.java)
                            val orderText = "${orderInDb.mainCourse} (${formatPrice(orderInDb.mainCoursePrice)} lei), " +
                                    "${orderInDb.sideDish} (${formatPrice(orderInDb.sideDishPrice)} lei), " +
                                    "${orderInDb.salad} (${formatPrice(orderInDb.saladPrice)} lei), " +
                                    "Total: ${formatPrice(orderInDb.totalPrice)} lei"

                            if (orderText == order) {
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        ordersList.removeAt(position)
                                        ordersAdapter.notifyDataSetChanged()
                                        Toast.makeText(this, "Order deleted successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to delete order: $e", Toast.LENGTH_SHORT).show()
                                    }
                                break
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this@OrderActivity, "Failed to load orders: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this@OrderActivity, "No email associated with the current user.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@OrderActivity, "No user is currently logged in.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun formatPrice(price: Double?): String {
        return if (price != null) decimalFormat.format(price) else ""
    }

    data class MeniuAles2(
        val email: String? = null,
        val mainCourse: String? = null,
        val sideDish: String? = null,
        val salad: String? = null,
        val mainCoursePrice: Double? = null,
        val sideDishPrice: Double? = null,
        val saladPrice: Double? = null,
        val totalPrice: Double? = null
    )
}
