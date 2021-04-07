package com.heycode.aizi.dashboard.identify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.heycode.aizi.R
import com.heycode.aizi.models.PeopleModel

class IdentifyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    //firebase
    private lateinit var mFirestore: FirebaseFirestore

    private var peopleAdapter: PeopleRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Identify People"

        //firebase
        val user = FirebaseAuth.getInstance().currentUser
        mFirestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.identify_recycler_view)

        val path = "peoples" + user!!.uid
        val query: Query = mFirestore.collection(path)
        val allPeoples: FirestoreRecyclerOptions<PeopleModel> =
            FirestoreRecyclerOptions.Builder<PeopleModel>()
                .setQuery(query, PeopleModel::class.java)
                .build()

        peopleAdapter = PeopleRVAdapter(allPeoples, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = peopleAdapter

    }

    override fun onStart() {
        super.onStart()
        peopleAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        peopleAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        peopleAdapter!!.stopListening()
    }

}