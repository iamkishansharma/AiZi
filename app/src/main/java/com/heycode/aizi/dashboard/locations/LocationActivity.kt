package com.heycode.aizi.dashboard.locations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.heycode.aizi.R
import com.heycode.aizi.models.LocationModel

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

        //firebase
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