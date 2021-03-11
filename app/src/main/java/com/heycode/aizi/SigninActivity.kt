package com.heycode.aizi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SigninActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private var currentUser: FirebaseUser? = null

    //firebase
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_activity)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth?.currentUser

        email = findViewById(R.id.signin_email)
        password = findViewById(R.id.signin_password)

        findViewById<Button>(R.id.signin_btn).setOnClickListener {
            if (
                checkErrors(
                    email,
                    password
                )
            ) {
                signInWith(email.text.toString(), password.text.toString())
            }
        }

        //Don't have an account navigate to SignUp Screen
        findViewById<TextView>(R.id.signup_text).setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }


    private fun checkErrors(
        email: EditText,
        password: EditText
    ): Boolean {
        if (email.text.isNullOrEmpty()) {
            email.error = "Required!"
            return false
        }
        if (password.text.isNullOrEmpty()) {
            password.error = "Required!"
            return false
        }
        return true
    }

    private fun signInWith(email: String, password: String) {
        if (password.length >= 6) {
            mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Try Again !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(this, "SignIn Task Failed!", Toast.LENGTH_SHORT).show()
        }
    }
}