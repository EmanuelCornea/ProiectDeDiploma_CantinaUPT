package com.example.proiectdediploma_cantinaupt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {

    private lateinit var userName: EditText
    private lateinit var userAdress: EditText
    private lateinit var userPhoneNumber: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button
    private lateinit var changeProfileImageButton: Button
    private lateinit var profileImageView: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        userName = findViewById(R.id.userName)
        userAdress = findViewById(R.id.userAdress)
        userPhoneNumber = findViewById(R.id.userPhoneNumber)
        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.backButton)
        changeProfileImageButton = findViewById(R.id.changeProfileImageButton)
        profileImageView = findViewById(R.id.profileImageView)


        changeProfileImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }


        saveButton.setOnClickListener {
            val name = userName.text.toString()
            val address = userAdress.text.toString()
            val phoneNumber = userPhoneNumber.text.toString()
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val userData = hashMapOf(
                    "name" to name,
                    "address" to address,
                    "phone" to phoneNumber,

                )
                db.collection("personal_profile").document(userId)
                    .set(userData)
                    .addOnSuccessListener {

                        if (imageUri != null) {
                            val profileImageRef = storage.reference.child("profile_images").child("$userId.jpg")
                            profileImageRef.putFile(imageUri!!)
                                .addOnSuccessListener { taskSnapshot ->

                                }
                                .addOnFailureListener { e ->

                                }
                        }
                    }
                    .addOnFailureListener { e ->

                    }
            }
        }


        backButton.setOnClickListener {
            finish()
        }


        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("personal_profile").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        val address = document.getString("address")
                        val phoneNumber = document.getString("phone")


                        userName.setText(name)
                        userAdress.setText(address)
                        userPhoneNumber.setText(phoneNumber)

                    }
                }
                .addOnFailureListener { e ->

                }
        }


        if (userId != null) {
            val profileImageRef = storage.reference.child("profile_images").child("$userId.jpg")
            profileImageRef.downloadUrl.addOnSuccessListener { uri ->

                Glide.with(this)
                    .load(uri)
                    .into(profileImageView)
            }.addOnFailureListener { exception ->

                Log.e("TAG", "Eșec la descărcarea imaginii", exception)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data

            profileImageView.setImageURI(imageUri)
        }
    }
}
