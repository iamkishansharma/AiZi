package com.heycode.aizi.dashboard.support

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.heycode.aizi.R
import com.heycode.aizi.models.LocationModel
import com.heycode.aizi.models.PeopleModel
import com.heycode.aizi.models.RoutineModel
import java.io.ByteArrayOutputStream


class SupportActivity : AppCompatActivity() {
    private lateinit var autoTextView: AutoCompleteTextView
    private lateinit var layoutOne: LinearLayout
    private lateinit var layoutTwo: LinearLayout
    private lateinit var layoutThree: LinearLayout
    private lateinit var addButton: Button

    //Add Person to remember
    private lateinit var personName: TextInputEditText
    private lateinit var personPhoneNumber: TextInputEditText
    private lateinit var personImage: ShapeableImageView

    //Add location to remember
    private lateinit var placeName: TextInputEditText
    private lateinit var placeCoordinates: TextInputEditText
    private lateinit var placeImage: ShapeableImageView

    //Add Daily Routine
    private lateinit var routineName: TextInputEditText
    private lateinit var routineTime: TextInputEditText
    private lateinit var routineDate: TextInputEditText


    private var imageUri: Uri? = null
    private var imageSelected1: Boolean = false
    private var imageSelected2: Boolean = false

    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStorage: FirebaseStorage
    private lateinit var mFirestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "AiZi Support"

        //firebase
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        mStorage = FirebaseStorage.getInstance()

        layoutOne = findViewById(R.id.first_layout)
        layoutTwo = findViewById(R.id.second_layout)
        layoutThree = findViewById(R.id.third_layout)
        addButton = findViewById(R.id.support_add_button)

        autoTextView = findViewById(R.id.select_feature)

        //Select option features
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.supportOption)
        )
        autoTextView.setAdapter(adapter)
        autoTextView.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    supportActionBar?.title = "Add People to Identify"
                    layoutOne.visibility = View.VISIBLE
                    layoutTwo.visibility = View.GONE
                    layoutThree.visibility = View.GONE
                    findViewById<LinearLayout>(R.id.zero_layout).visibility = View.GONE

                    personImage = findViewById(R.id.support_person_image)
                    personName = findViewById(R.id.support_person_name)
                    personPhoneNumber = findViewById(R.id.support_person_number)
                    personImage.setOnClickListener {
                        val gallery =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        startActivityForResult(gallery, 102)
                    }

                    addButton.setOnClickListener {
                        if (
                            checkErrors(
                                imageSelected1,
                                personName,
                                personPhoneNumber,
                                personName
                            )
                        ) {
                            Toast.makeText(this, "1: Uploading....", Toast.LENGTH_SHORT).show()
                            uploadImage(
                                personImage,
                                "peoples",
                                personName,
                                personPhoneNumber
                            )
                        } else {
                            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                1 -> {
                    supportActionBar?.title = "Add Location"
                    layoutOne.visibility = View.GONE
                    layoutTwo.visibility = View.VISIBLE
                    layoutThree.visibility = View.GONE
                    findViewById<LinearLayout>(R.id.zero_layout).visibility = View.GONE

                    placeImage = findViewById(R.id.support_place_image)
                    placeName = findViewById(R.id.support_place_name)
                    placeCoordinates = findViewById(R.id.support_place_coordinates)

                    placeImage.setOnClickListener {
                        val gallery =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        startActivityForResult(gallery, 103)
                    }
                    addButton.setOnClickListener {
                        if (
                            checkErrors(
                                imageSelected2,
                                placeName,
                                placeCoordinates,
                                placeName
                            )
                        ) {
                            uploadImage(
                                placeImage,
                                "locations",
                                placeName,
                                placeCoordinates
                            )
                        } else {
                            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                ////////////////Third UI
                2 -> {
                    supportActionBar?.title = "Add Daily Activity"
                    layoutOne.visibility = View.GONE
                    layoutTwo.visibility = View.GONE
                    layoutThree.visibility = View.VISIBLE
                    findViewById<LinearLayout>(R.id.zero_layout).visibility = View.GONE

                    routineName = findViewById(R.id.support_daily_title)
                    routineDate = findViewById(R.id.support_daily_date)
                    routineTime = findViewById(R.id.support_daily_time)
                    routineDate.setOnClickListener {
                        hideKeyboard(routineName)
                        val dialogInterface: DialogInterface = object : DialogInterface {
                            override fun cancel() {
                            }

                            override fun dismiss() {
                            }
                        }
                        val datePickerDialog = DatePickerDialog(this)
                        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                            routineDate.setText(String.format("%d/%d/%d", year, month, dayOfMonth))
                        }
                        datePickerDialog.onClick(dialogInterface, 1)
                        datePickerDialog.show()
                    }

                    routineTime.setOnClickListener {
                        val dialogInterface: DialogInterface = object : DialogInterface {
                            override fun cancel() {
                            }

                            override fun dismiss() {
                            }
                        }

                        val timePickerDialog = TimePickerDialog(
                            this,
                            { view, hourOfDay, minute ->
                                routineTime.setText(
                                    String.format(
                                        "%d:%d",
                                        hourOfDay,
                                        minute
                                    )
                                )
                            }, 0, 0, false
                        )
                        timePickerDialog.onClick(dialogInterface, 1)
                        timePickerDialog.show()
                    }


                    addButton.setOnClickListener {
                        if (
                            checkErrors(
                                true,
                                routineName,
                                routineDate,
                                routineTime
                            )
                        ) {
                            saveData(
                                "routines",
                                routineName.text.toString().trim(),
                                routineDate.text.toString(),
                                routineTime.text.toString()
                            )
                        } else {
                            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


    }

    private fun checkErrors(
        isSelected: Boolean,
        name: TextInputEditText,
        number: TextInputEditText,
        more: TextInputEditText
    ): Boolean {
        if (!isSelected) {
            Toast.makeText(this, "Select Image first", Toast.LENGTH_LONG).show()
            return false
        }
        if (name.text.isNullOrEmpty()) {
            name.error = "Required!"
            return false
        }
        if (number.text.isNullOrEmpty()) {
            name.error = "Required!"
            return false
        }
        if (name.text.isNullOrEmpty()) {
            name.error = "Required!"
            return false
        }
        if (more.text.isNullOrEmpty()) {
            more.error = "Required!"
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 102) {
            imageUri = data?.data
            personImage.setImageURI(imageUri)
            imageSelected1 = true
        }
        if (resultCode == RESULT_OK && requestCode == 103) {
            imageUri = data?.data
            placeImage.setImageURI(imageUri)
            imageSelected2 = true
        }
    }

    //upload user's profile pic
    private fun uploadImage(
        imageView: ShapeableImageView,
        toFolder: String,
        name: TextInputEditText,
        number: TextInputEditText
    ) {
        imageView.setDrawingCacheEnabled(true)
        imageView.buildDrawingCache()
        val bitmap: Bitmap = imageView.getDrawingCache(true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        imageView.setDrawingCacheEnabled(false)
        val bytes = byteArrayOutputStream.toByteArray()
        val user: FirebaseUser? = mAuth.currentUser
        //saving in mentioned dir
        val path = user?.uid + "/$toFolder" + "/${name.text.toString()}.png"
        val reference: StorageReference = mStorage.getReference(path)
        val metadata = StorageMetadata.Builder()
            .setCustomMetadata("text", "Profile pic of ${user?.displayName}").build()

        reference.putBytes(bytes, metadata).addOnSuccessListener { taskSnapshot ->
            //getting image url for showing user's profile pic with Glide
            reference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl: String = uri.toString()

                //saving user's data
                //navigating to different screen
                saveData(
                    toFolder,
                    name.text.toString().trim(),
                    number.text.toString().trim(),
                    imageUrl
                )
            }
        }
    }

    private fun saveData(to: String, name: String, number: String, imageUrl: String) {

        val user: FirebaseUser? = mAuth.currentUser
        val reference: DocumentReference =
            mFirestore.collection("$to${user?.uid}").document()

        if (to == "peoples") {
            val userData = PeopleModel(name, number, imageUrl)
            if (user != null) {
                reference.set(userData).addOnSuccessListener {
                    Toast.makeText(this, "$name added", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
        if (to == "locations") {
            val latlong: List<String> = number.split(",")
            val userData =
                LocationModel(imageUrl, name, latlong[0], latlong[1])
            if (user != null) {
                val reference: DocumentReference =
                    mFirestore.collection("$to${user.uid}").document()

                reference.set(userData).addOnSuccessListener {
                    Toast.makeText(this, "$name added", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
        if (to == "routines") {
//            Here name is TITLE
//            number is DATE
//            imageUrl is TIME
            val userData = RoutineModel(name, number, imageUrl, "no")
            if (user != null) {
                reference.set(userData).addOnSuccessListener {
                    Toast.makeText(this, "$name added", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun hideKeyboard(editText: TextInputEditText?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText!!.windowToken, 0)
    }
}