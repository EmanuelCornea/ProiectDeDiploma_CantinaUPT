package com.example.proiectdediploma_cantinaupt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectdediploma_cantinaupt.databinding.ActivitySignInBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        binding.button.setOnClickListener {

            val email = binding.emailUser.text.toString()
            val pass = binding.passUser.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid
                        if (userId != null) {
                            firestore.collection("users").document(userId).get().addOnSuccessListener { document ->
                                val role = document.getString("role")
                                if (role != null) {
                                    when (role) {
                                        "Admin" -> {
                                            val intent = Intent(this, AdminMainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        "Student" -> {
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        else -> {
                                            Toast.makeText(this, "Unknown user role", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(this, "User role not found", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener { e ->
                                Toast.makeText(this, "Error accessing user data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
