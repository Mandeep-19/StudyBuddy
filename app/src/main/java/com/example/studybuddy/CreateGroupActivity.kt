package com.example.studybuddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateGroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var etSubject: EditText
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var etLocation: EditText
    private lateinit var btnSaveGroup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etSubject = findViewById(R.id.etSubject)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        etLocation = findViewById(R.id.etLocation)
        btnSaveGroup = findViewById(R.id.btnSaveGroup)

        btnSaveGroup.setOnClickListener {
            saveStudyGroup()
        }
    }

    private fun saveStudyGroup() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "You must be logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val subject = etSubject.text.toString().trim()
        val date = etDate.text.toString().trim()
        val time = etTime.text.toString().trim()
        val location = etLocation.text.toString().trim()

        if (subject.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val data = hashMapOf(
            "hostId" to user.uid,
            "subject" to subject,
            "date" to date,
            "time" to time,
            "location" to location,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("StudyGroups")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Study session saved", Toast.LENGTH_SHORT).show()
                finish() // go back to HomeActivity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
