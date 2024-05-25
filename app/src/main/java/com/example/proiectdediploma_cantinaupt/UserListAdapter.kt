package com.example.proiectdediploma_cantinaupt


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class UserListAdapter(private val onDeleteClickListener: (user: User) -> Unit) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private val userList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.bind(currentUser)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUsers(users: List<User>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        private val roleTextView: TextView = itemView.findViewById(R.id.roleTextView)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(user: User) {
            emailTextView.text = user.email
            roleTextView.text = user.role

            deleteButton.setOnClickListener {
                onDeleteClickListener.invoke(user)
            }
        }
    }

}
