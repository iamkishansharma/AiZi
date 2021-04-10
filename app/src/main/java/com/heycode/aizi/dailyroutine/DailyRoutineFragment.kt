package com.heycode.aizi.dailyroutine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.heycode.aizi.R
import com.heycode.aizi.models.RoutineModel

class DailyRoutineFragment : Fragment() {

    //firebase
    private lateinit var mFirestore: FirebaseFirestore
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var recyclerView: RecyclerView
    private var dailyAdapter: DailyRecyclerViewAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_daily_routine, container, false)
        recyclerView = rootView.findViewById(R.id.daily_recycler_view)

        mFirestore = FirebaseFirestore.getInstance()
        //RecyclerView setup from firebaseUI
        val path = "routines" + user!!.uid
        val query: Query =
            mFirestore.collection(path)

        val allRoutines: FirestoreRecyclerOptions<RoutineModel> =
            FirestoreRecyclerOptions.Builder<RoutineModel>()
                .setQuery(query, RoutineModel::class.java)
                .build()

        dailyAdapter = activity?.let { DailyRecyclerViewAdapter(allRoutines, it) }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = dailyAdapter

        return rootView
    }

    override fun onStart() {
        super.onStart()
        dailyAdapter!!.startListening()
    }

    override fun onPause() {
        super.onPause()
        dailyAdapter!!.stopListening()
    }

    override fun onResume() {
        super.onResume()
        dailyAdapter!!.startListening()
    }
}