package com.heycode.aizi.dashboard.appointment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.heycode.aizi.R
import com.heycode.aizi.models.AppointmentModel


class AppointmentRVAdapter(
    options: FirestoreRecyclerOptions<AppointmentModel>,
    var context: Context
) :
    FirestoreRecyclerAdapter<AppointmentModel, AppointmentRVAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.appointment_image)
        var name: TextView = itemView.findViewById(R.id.appoint_doc_name)
        var title: TextView = itemView.findViewById(R.id.appoint_doc_spec)
        var work: TextView = itemView.findViewById(R.id.appoint_doc_work)
        var address: TextView = itemView.findViewById(R.id.appoint_doc_location)
        var time: TextView = itemView.findViewById(R.id.appoint_text_time)
        var date: TextView = itemView.findViewById(R.id.appoint_text_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
        model: AppointmentModel
    ) {
        Glide.with(context)
            .load(model.image)
            .centerCrop()
            .into(holder.imageView)

        holder.name.text = model.name
        holder.title.text = model.title
        holder.address.text = model.address
        holder.work.text = model.work
        holder.time.text = model.time
        holder.date.text = model.date
    }
}