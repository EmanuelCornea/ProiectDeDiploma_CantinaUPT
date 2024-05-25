package com.example.proiectdediploma_cantinaupt

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserListActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        fetchUsers()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.userRecyclerView)
        userListAdapter = UserListAdapter { user -> deleteUser(user) }
        recyclerView.adapter = userListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchUsers() {
        firestore.collection("users").get()
            .addOnSuccessListener { querySnapshot ->
                val users = mutableListOf<User>()
                for (document in querySnapshot.documents) {
                    val email = document.getString("email") ?: ""
                    val role = document.getString("role") ?: ""
                    if (role != "Admin") {
                        val id = document.id
                        val user = User(id, email, role)
                        users.add(user)
                    }
                }
                userListAdapter.updateUsers(users)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch users", exception)
                Toast.makeText(this, "Failed to fetch users: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteUser(user: User) {
        val userId = user.id

        firestore.collection("users").document(userId).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "User $userId deleted from Firestore", Toast.LENGTH_SHORT).show()
                fetchUsers()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to delete user $userId from Firestore", exception)
                Toast.makeText(this, "Failed to delete user $userId from Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    companion object {
        private const val TAG = "UserListActivity"
    }
}
