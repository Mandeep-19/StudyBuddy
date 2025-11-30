package com.example.studybuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var tvWelcome: TextView
    private lateinit var tvGroups: TextView
    private lateinit var btnCreateGroup: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        tvWelcome = findViewById(R.id.tvWelcome)
        tvGroups = findViewById(R.id.tvGroups)
        btnCreateGroup = findViewById(R.id.btnCreateGroup)
        btnLogout = findViewById(R.id.btnLogout)

        val user = auth.currentUser
        tvWelcome.text = "Welcome to StudyBuddy\n${user?.email ?: ""}"

        btnCreateGroup.setOnClickListener {
            startActivity(Intent(this, CreateGroupActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadStudyGroups()
    }

    private fun loadStudyGroups() {
        val user = auth.currentUser ?: return

        db.collection("StudyGroups")
            .whereEqualTo("hostId", user.uid)
            .get()
            .addOnSuccessListener { query ->
                val builder = StringBuilder()
                for (doc in query) {
                    val subject = doc.getString("subject") ?: ""
                    val date = doc.getString("date") ?: ""
                    val time = doc.getString("time") ?: ""
                    val location = doc.getString("location") ?: ""

                    builder.append("- $subject @ $time on $date in $location\n")
                }
                tvGroups.text = if (builder.isNotEmpty()) builder.toString() else "No sessions yet."
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading sessions: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
