package com.heycode.aizi

import android.content.Intent
import android.graphics.Bitmap
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class SignupActivity : AppCompatActivity() {
    private lateinit var imageView: ShapeableImageView
    private lateinit var full_name: EditText
    private lateinit var email_: EditText
    private lateinit var password_: EditText
    private var imageUri: Uri? = null
    private var imageSelected: Boolean = false
    private lateinit var imageSelectError: TextView

    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStorage: FirebaseStorage
    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //firebase
        mStorage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        imageSelectError = findViewById(R.id.image_error_text)
        full_name = findViewById(R.id.signup_name)
        email_ = findViewById(R.id.signup_email)
        password_ = findViewById(R.id.signup_password)

        imageView = findViewById(R.id.signup_image_upload)
        imageView.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 101)
        }

        findViewById<Button>(R.id.signup_btn).setOnClickListener {
            if (
                checkErrors(
                    imageSelected,
                    full_name,
                    email_,
                    password_
                )
            ) {
                signUpWith(email_.text.toString(), password_.text.toString())
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

    private fun signUpWith(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user: FirebaseUser? = mAuth.currentUser
                if (user != null) {
                    //saving user's data
                    saveData(full_name.text.toString(), email_.text.toString(), "2021/12/13")
                    //uploading the profile image
                    uploadImage(imageView)
                    Toast.makeText(this, "Welcome to AiZi", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    this, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //upload user's profile pic
    private fun uploadImage(imageView: ShapeableImageView) {
        imageView.setDrawingCacheEnabled(true)
        imageView.buildDrawingCache()
        val bitmap: Bitmap = imageView.getDrawingCache(true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        imageView.setDrawingCacheEnabled(false)
        val bytes = byteArrayOutputStream.toByteArray()
        val user: FirebaseUser? = mAuth.currentUser
        //saving in mentioned dir
        val path = user?.uid + "/profile.png"
        val reference: StorageReference = mStorage.getReference(path)
        val metadata = StorageMetadata.Builder()
            .setCustomMetadata("text", "Profile pic of ${full_name.text}").build()

        val uploadTask = reference.putBytes(bytes, metadata) //image & text
        uploadTask.addOnSuccessListener { taskSnapshot ->
            val url = taskSnapshot.metadata!!
                .reference!!.downloadUrl
            Toast.makeText(this, "Url: $url", Toast.LENGTH_LONG).show()
        }
    }

    fun saveData(fullName: String, email: String, dob: String) {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["name"] = fullName
        hashMap["email"] = email
        hashMap["dob"] = dob

        val user: FirebaseUser? = mAuth.currentUser
        if (user != null) {
            mDatabaseReference.child("Users").child(user.uid).updateChildren(hashMap)
        } else {
            Toast.makeText(this, "Can't save the user data", Toast.LENGTH_SHORT).show()
        }
    }
}