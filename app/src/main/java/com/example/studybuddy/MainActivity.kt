package com.example.studybuddy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Already logged in → go to Home
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            // Not logged in → go to Login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        // No UI here, just routing
        finish()
    }
}
