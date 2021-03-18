package com.heycode.aizi.dashboard.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.heycode.aizi.R

class AddTaskActivity : AppCompatActivity() {
    private lateinit var title: EditText
    private lateinit var description: EditText

    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        //ActionBar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Add new Task"

        //firebase
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        title = findViewById(R.id.editText_title)
        description = findViewById(R.id.editText_description)


        findViewById<Button>(R.id.save_button).setOnClickListener {
            if (title.text.isNullOrEmpty()) {
                title.error = "Required!"
                return@setOnClickListener
            }
            if (description.text.isNullOrEmpty()) {
                description.error = "Required!"
                return@setOnClickListener
            }
            saveTaskData(
                title.text.toString(),
                description.text.toString(),
                "no"
            )
        }
    }

    private fun saveTaskData(title: String, description: String, isCompleted: String) {
        val user: FirebaseUser? = mAuth.currentUser
        val reference: DocumentReference = mFirestore.collection("${user?.uid}").document()

        val data = HashMap<String, Any>()
        data["title"] = title
        data["description"] = description
        data["completed"] = isCompleted

        reference.set(data).addOnSuccessListener {
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show()
        }
    }
}