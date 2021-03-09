package com.heycode.aizi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_activity)

        findViewById<Button>(R.id.signin_btn).setOnClickListener {
            if (
                checkErrors(
                    findViewById(R.id.signin_email),
                    findViewById(R.id.signin_password)
                )
            ) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()

                //TODO:: Add firebase login here then start the activity
                startActivity(Intent(this, MainActivity::class.java))
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
}