package com.example.proiectdediploma_cantinaupt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderListAdapter(
    private var orders: MutableList<MeniuAles>,
    private val deleteOrder: (MeniuAles) -> Unit
) : RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCourseTextView: TextView = itemView.findViewById(R.id.mainCourseTextView)
        val sideDishTextView: TextView = itemView.findViewById(R.id.sideDishTextView)
        val saladTextView: TextView = itemView.findViewById(R.id.saladTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.mainCourseTextView.text = order.mainCourse
        holder.sideDishTextView.text = order.sideDish
        holder.saladTextView.text = order.salad

        holder.deleteButton.setOnClickListener {
            deleteOrder(order)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun updateOrders(newOrders: List<MeniuAles>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }
}
