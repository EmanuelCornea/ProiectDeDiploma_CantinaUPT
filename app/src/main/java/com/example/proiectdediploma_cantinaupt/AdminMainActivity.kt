package com.example.proiectdediploma_cantinaupt


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectdediploma_cantinaupt.databinding.ActivityAdminMainBinding


import com.google.firebase.auth.FirebaseAuth


class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        binding.UserListButton.setOnClickListener{
            startActivity(
                Intent(this, UserListActivity::class.java)
            )
        }

        binding.meniuEditButton.setOnClickListener{
            startActivity(
                Intent(this, MenuEditActivity::class.java)
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

        binding.orderListButton.setOnClickListener{
            startActivity(
                Intent(this, OrderListActivity::class.java)
            )
        }

        binding.ViewGraphButton.setOnClickListener{
            startActivity(
                Intent(this, ViewGraphActivity::class.java)
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
