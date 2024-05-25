package com.example.proiectdediploma_cantinaupt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminConfirmationActivity : AppCompatActivity() {

    private lateinit var confirmationCodeEditText: EditText
    private lateinit var confirmButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_confirmation)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        confirmationCodeEditText = findViewById(R.id.codeEditText)
        confirmButton = findViewById(R.id.confirmButton)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        confirmButton.setOnClickListener {
            val enteredCode = confirmationCodeEditText.text.toString()

            if (enteredCode.isEmpty()) {
                Toast.makeText(this, "Please enter a confirmation code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            verifyConfirmationCode(enteredCode, email, password)
        }
    }

    private fun verifyConfirmationCode(code: String, email: String?,password: String?) {
        firestore.collection("confirmation_codes").document(code).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "Code verified, user approved!", Toast.LENGTH_SHORT).show()
                    registerAdmin(email,password)
                } else {
                    Toast.makeText(this, "Invalid confirmation code", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error verifying code: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }




    private fun registerAdmin(email: String?, password: String?) {
        email?.let { userEmail ->
            password?.let { userPassword ->
                firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val userData = hashMapOf(
                                "email" to userEmail,
                                "role" to "Admin"
                            )
                            user?.let { currentUser ->
                                firestore.collection("users").document(currentUser.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        val intent = Intent(this, AdminMainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { exception ->
                                        Toast.makeText(
                                            this,
                                            "Failed to register admin: ${exception.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Failed to create admin: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

}
