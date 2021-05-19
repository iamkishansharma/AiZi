package com.heycode.aizi.dashboard.appointment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.heycode.aizi.R
import com.heycode.aizi.models.AppointmentModel

class AppointmentActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    //firebase
    private lateinit var mFirestore: FirebaseFirestore

    private var appointAdapter: AppointmentRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)
        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Appointments"

        recyclerView = findViewById(R.id.appointment_recycler_view)

        //firebase
        val user = FirebaseAuth.getInstance().currentUser
        mFirestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.appointment_recycler_view)

        val path = "appointments" + user!!.uid
        val query: Query = mFirestore.collection(path)
        val allAppointments: FirestoreRecyclerOptions<AppointmentModel> =
            FirestoreRecyclerOptions.Builder<AppointmentModel>()
                .setQuery(query, AppointmentModel::class.java)
                .build()

        appointAdapter = AppointmentRVAdapter(allAppointments, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = appointAdapter

    }


    override fun onStart() {
        super.onStart()
        appointAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        appointAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        appointAdapter!!.stopListening()
    }
}