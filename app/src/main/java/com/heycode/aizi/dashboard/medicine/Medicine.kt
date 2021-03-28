package com.heycode.aizi.dashboard.medicine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.heycode.aizi.R
import com.heycode.aizi.dashboard.identify.PeopleRVAdapter

class Medicine : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    //firebase
    private lateinit var mFirestore: FirebaseFirestore

    private var medicineAdapter: MedicineRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Medicine To Take Today"

        //firebase
        val user = FirebaseAuth.getInstance().currentUser
        mFirestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.medicine_recycler_view)
    }
}