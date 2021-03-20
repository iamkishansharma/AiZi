package com.heycode.aizi.dashboard.locations

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heycode.aizi.R

class LocationActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Important Locations"


        recyclerView = findViewById(R.id.location_recycler_view)
        val adapter =
            MyRecyclerViewAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }


    class MyRecyclerViewAdapter(var context: Context) :
        RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageTitle: TextView = itemView.findViewById(R.id.location_item_title)
            var imageLocation: TextView = itemView.findViewById(R.id.location_item_title2)
            var image: ImageView = itemView.findViewById(R.id.location_item_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.image.setImageDrawable(context.resources.getDrawable(R.drawable.female_doctor))
            holder.imageTitle.text = "My Home"
            holder.imageLocation.text = "Show in Google Map"
            holder.itemView.findViewById<CardView>(R.id.location_item_total).setOnClickListener {
                Toast.makeText(
                    context,
                    holder.itemView.findViewById<TextView>(R.id.location_item_title).text.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        override fun getItemCount(): Int {
            return 10
        }


    }
}