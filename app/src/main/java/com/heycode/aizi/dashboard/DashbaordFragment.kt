package com.heycode.aizi.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.heycode.aizi.R
import com.heycode.aizi.dashboard.identify.IdentifyActivity
import com.heycode.aizi.dashboard.locations.LocationActivity
import com.heycode.aizi.dashboard.todo.TodoActivity


class DashbaordFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_dashbaord, container, false)

        rootView.findViewById<CardView>(R.id.card_identify).setOnClickListener {
            startActivity(Intent(activity, IdentifyActivity::class.java))
        }
        rootView.findViewById<CardView>(R.id.card_location).setOnClickListener {
            startActivity(Intent(activity, LocationActivity::class.java))
        }
        rootView.findViewById<CardView>(R.id.card_todo).setOnClickListener {
            startActivity(Intent(activity, TodoActivity::class.java))
        }
        rootView.findViewById<CardView>(R.id.card_training).setOnClickListener {
//            startActivity(Intent(activity, TodoActivity::class.java))
        }
        return rootView
    }

}