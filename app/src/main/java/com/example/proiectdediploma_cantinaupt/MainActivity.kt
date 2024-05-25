package com.example.proiectdediploma_cantinaupt

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile

import androidx.appcompat.app.AppCompatActivity
import com.example.proiectdediploma_cantinaupt.databinding.ActivityMainBinding


import com.google.firebase.auth.FirebaseAuth



class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  lateinit var user: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()



      binding.meniuButton.setOnClickListener{
          startActivity(
              Intent(this, MeniuActivity::class.java)
          )

      }
        binding.QRbuttonEntry.setOnClickListener{
            startActivity(
                Intent(this, QRCodeActivity::class.java)
            )

        }
        binding.profileButton.setOnClickListener{
            startActivity(
                Intent(this, ProfileActivity::class.java)
            )
        }


        binding.orderButton.setOnClickListener{
            startActivity(
                Intent(this, OrderActivity::class.java)
            )
        }

        binding.logout.setOnClickListener{
            user.signOut()
            startActivity(
                Intent(this, SignInActivity::class.java)
            )
            finish()
        }

    }
}


