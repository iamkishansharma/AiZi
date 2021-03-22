package com.heycode.aizi.dashboard.locations

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.heycode.aizi.R

class LocationActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var locationAdapter: LocationsRVAdapter? = null

    //firebase
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Important Locations"

        //fire
        val user = FirebaseAuth.getInstance().currentUser
        mFirestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.location_recycler_view)

        val path = "locations" + user!!.uid
        val query: Query = mFirestore.collection(path)
        val allLocations: FirestoreRecyclerOptions<LocationModel> =
            FirestoreRecyclerOptions.Builder<LocationModel>()
                .setQuery(query, LocationModel::class.java)
                .build()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        locationAdapter = LocationsRVAdapter(allLocations, this)
        recyclerView.adapter = locationAdapter

        if (mFirestore.collection(path).id.isNullOrEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Not allowed to use this feature!")
                .setCancelable(false)
                .setMessage("To get access send an email to ADMIN containing details like Image, Name and Geo location.")
                .setIcon(R.drawable.logo)
                .setPositiveButton(
                    "Send Mail"
                ) { _, _ ->

                    Toast.makeText(
                        this,
                        "Opening in GMail",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_EMAIL, arrayOf("heycodeinc@gmail.com"))
                        .setType("text/html")
                        .setPackage("com.google.android.gm")
                        .putExtra(
                            Intent.EXTRA_SUBJECT,
                            "Register me for Locations Feature in AiZi"
                        )
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Kindly provide me access to Important Locations feature in AiZi app." +
                                " I have attached required details like Location name, Image with Latitude & Longitude."
                    )
                    finish()
                    startActivity(intent)
                }
                .setNegativeButton(
                    "Back"
                ) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    finish()
                }
            val dialog = builder.create()
            dialog.show()
        }

    }

    override fun onStart() {
        super.onStart()
        locationAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        locationAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationAdapter!!.stopListening()
    }

}