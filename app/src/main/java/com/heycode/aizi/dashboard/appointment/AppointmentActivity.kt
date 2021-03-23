package com.heycode.aizi.dashboard.appointment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heycode.aizi.R

class AppointmentActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)
        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Appointments"

        recyclerView = findViewById(R.id.appointment_recycler_view)

        val adapter = RVAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

    }

    class RVAdapter(context: Context) : RecyclerView.Adapter<RVAdapter.RVViewHolder>() {
        class RVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val doctor: TextView = itemView.findViewById(R.id.appoint_doc_name)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
            return RVViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_appointment, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
            holder.doctor.text = "Dr. Tony Stark"
        }

        override fun getItemCount(): Int {
            return 10
        }
    }
}