package com.heycode.aizi.dashboard.locations

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.heycode.aizi.R
import java.util.*

class LocationsRVAdapter(options: FirestoreRecyclerOptions<LocationModel>, var context: Context) :
    FirestoreRecyclerAdapter<LocationModel, LocationsRVAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.location_item_image)
        var title: TextView = itemView.findViewById(R.id.location_item_title)
        var latlong: TextView = itemView.findViewById(R.id.location_item_title2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: LocationModel) {
        Glide.with(context)
            .load(model.image)
            .centerCrop()
            .into(holder.imageView)

        holder.title.text = model.title

        holder.latlong.text = model.latitude + ", " + model.longitude

        holder.itemView.findViewById<CardView>(R.id.location_item_total).setOnClickListener {
            val location: String =
                holder.itemView.findViewById<TextView>(R.id.location_item_title2).text.toString()
            val latlong: List<String> = location.split(",")
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    String.format(
                        Locale.ENGLISH,
                        "google.navigation:q=%s,%s",
                        latlong[0],
                        latlong[1]
                    )
                )
            )
            Toast.makeText(context, "Showing on GoogleMaps", Toast.LENGTH_LONG).show()
            intent.setPackage("com.google.android.apps.maps")
            context.startActivity(intent)
        }
    }

}