package com.example.proiectdediploma_cantinaupt

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectdediploma_cantinaupt.databinding.ActivitySignUpBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import java.util.UUID

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val roles = arrayOf("Student", "Admin")
    private var selectedRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.roleSignup.adapter = adapter

        binding.button.setOnClickListener {
            val email = binding.emailUser.text.toString()
            val pass = binding.passUser.text.toString()
            val confirmPass = binding.confirmPassUser.text.toString()
            selectedRole = binding.roleSignup.selectedItem.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && selectedRole != null) {
                if (pass == confirmPass) {
                    verifyEmailAndRegister(email, pass)
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyEmailAndRegister(email: String, password: String) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
            .addOnSuccessListener { result ->
                if (result.signInMethods?.isNotEmpty() == true) {

                    Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
                } else {

                    if (selectedRole == "Admin") {
                        val confirmationCode = generateConfirmationCode()
                        saveConfirmationCode(confirmationCode)
                        val intent = Intent(this, AdminConfirmationActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("password", password)
                        intent.putExtra("confirmation_code", confirmationCode)
                        startActivity(intent)
                    } else {
                        registerUser(email, password)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error checking email: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun generateConfirmationCode(): String {
        return UUID.randomUUID().toString().substring(0, 6)
    }

    private fun saveConfirmationCode(code: String) {
        val confirmationData = hashMapOf(
            "code" to code,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("confirmation_codes").document(code).set(confirmationData)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid

                    userId?.let {
                        val user = hashMapOf(
                            "email" to email,
                            "role" to selectedRole
                        )

                        firestore.collection("users").document(it).set(user)
                            .addOnSuccessListener {
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user data: $e", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
}
