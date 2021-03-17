package com.heycode.aizi.dashboard.todo

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.heycode.aizi.R

class AddTaskActivity : AppCompatActivity() {
    private lateinit var title: EditText
    private lateinit var description: EditText

    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Add Tasks"

        //firebase
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        title = findViewById(R.id.editText_title)
        description = findViewById(R.id.editText_description)


        findViewById<Button>(R.id.buttonAddNote).setOnClickListener {
            if (title.text.isNullOrEmpty()) {
                title.error = "Required!"
                return@setOnClickListener
            }
            if (description.text.isNullOrEmpty()) {
                description.error = "Required!"
                return@setOnClickListener
            }
//            saveTaskData(
//                title.text.toString(),
//                description.text.toString(),
//                taskCompleted.isChecked
//            )
        }
    }

//    private fun saveTaskData(title: String, description: String, isCompleted: Boolean) {
//        val mTaskData = TaskDataModel(title, description, isCompleted)
//        val user: FirebaseUser? = mAuth.currentUser
//        if (user != null) {
//            mDatabaseReference.child("Users").child(user.uid).child("allTasks").setValue(mTaskData)
//            Toast.makeText(this, "Welcome to AiZi", Toast.LENGTH_SHORT).show()
//            finish()
//        } else {
//            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
//        }
//    }
}