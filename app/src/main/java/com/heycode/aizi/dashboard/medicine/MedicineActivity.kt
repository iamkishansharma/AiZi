package com.heycode.aizi.dashboard.medicine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.heycode.aizi.R
import com.heycode.aizi.models.MedicineModel

class MedicineActivity : AppCompatActivity() {

    //firebase
    private lateinit var mFirestore: FirebaseFirestore
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var recyclerView: RecyclerView
    private var medicineAdapter: MedicineRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "My Medicines"

        recyclerView = findViewById(R.id.medicine_recycler_view)

        mFirestore = FirebaseFirestore.getInstance()
        //RecyclerView setup from firebaseUI
        val path = "medicines" + user!!.uid
        val query: Query =
            mFirestore.collection(path)

        val allMedicines: FirestoreRecyclerOptions<MedicineModel> =
            FirestoreRecyclerOptions.Builder<MedicineModel>()
                .setQuery(query, MedicineModel::class.java)
                .build()

        medicineAdapter = MedicineRVAdapter(allMedicines, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = medicineAdapter
    }


    override fun onStart() {
        super.onStart()
        medicineAdapter!!.startListening()
    }

    override fun onPause() {
        super.onPause()
        medicineAdapter!!.stopListening()
    }

    override fun onResume() {
        super.onResume()
        medicineAdapter!!.startListening()
    }
}