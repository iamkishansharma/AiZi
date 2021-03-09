package com.heycode.aizi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView

class SignupActivity : AppCompatActivity() {
    private lateinit var imageView: ShapeableImageView
    private var imageUri: Uri? = null
    private var imageSelected: Boolean = false
    private lateinit var imageSelectError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        imageSelectError = findViewById(R.id.image_error_text)

        imageView = findViewById(R.id.signup_image_upload)
        imageView.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 101)
        }

        findViewById<Button>(R.id.signup_btn).setOnClickListener {
            if (
                checkErrors(
                    imageSelected,
                    findViewById(R.id.signup_name),
                    findViewById(R.id.signup_email),
                    findViewById(R.id.signup_password)
                )
            ) {
                if (signUpForApp()) {
                    Toast.makeText(this, "Account Created\nNow Login", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, SigninActivity::class.java))
                }
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
            }
        }

        //Already have an account navigate to SignIn Screen
        findViewById<TextView>(R.id.signin_text).setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }
    }

    private fun signUpForApp(): Boolean {
        // TODO:: Add firebase SignUP here then start the Sign In activity
        return true
    }

    private fun checkErrors(
        imageSelected: Boolean,
        fullName: EditText,
        email: EditText,
        password: EditText
    ): Boolean {
        if (!imageSelected) {
            imageSelectError.visibility = View.VISIBLE
            Toast.makeText(this, "Upload image", Toast.LENGTH_SHORT).show()
            return false
        }
        if (fullName.text.isNullOrEmpty()) {
            fullName.error = "Required!"
            return false
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 101) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            imageSelectError.visibility = View.INVISIBLE
            imageSelected = true
        }
    }
}