package com.example.proiectdediploma_cantinaupt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore



class OrderListActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var orderListAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        fetchOrders()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.orderRecyclerView)
        orderListAdapter = OrderListAdapter(mutableListOf()) { order ->
            deleteOrder(order)
        }
        recyclerView.adapter = orderListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchOrders() {
        firestore.collection("selected_menus").get()
            .addOnSuccessListener { querySnapshot ->
                val orders = mutableListOf<MeniuAles>()
                for (document in querySnapshot.documents) {
                    val mainCourse = document.getString("mainCourse") ?: ""
                    val sideDish = document.getString("sideDish") ?: ""
                    val salad = document.getString("salad") ?: ""
                    val price = document.getDouble("price") ?: 0.0
                    val order = MeniuAles(mainCourse, sideDish, salad, price)
                    orders.add(order)
                }
                orderListAdapter.updateOrders(orders)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load orders: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteOrder(order: MeniuAles) {
        firestore.collection("selected_menus")
            .whereEqualTo("mainCourse", order.mainCourse)
            .whereEqualTo("sideDish", order.sideDish)
            .whereEqualTo("salad", order.salad)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Order deleted", Toast.LENGTH_SHORT).show()
                            fetchOrders()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to delete order: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to find order: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
