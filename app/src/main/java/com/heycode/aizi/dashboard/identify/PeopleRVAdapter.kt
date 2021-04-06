package com.heycode.aizi.dashboard.identify

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.heycode.aizi.R
import com.heycode.aizi.models.PeopleModel


class PeopleRVAdapter(options: FirestoreRecyclerOptions<PeopleModel>, var context: Context) :
    FirestoreRecyclerAdapter<PeopleModel, PeopleRVAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.item_image_view)
        var name: TextView = itemView.findViewById(R.id.item_text_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: PeopleModel) {
        Glide.with(context)
            .load(model.image)
            .centerCrop()
            .into(holder.imageView)
        holder.name.text = model.name

        holder.itemView.findViewById<ImageButton>(R.id.item_call).setOnClickListener {
            val phone: String = model.phoneNo.toString()
            Toast.makeText(context, "Calling...\n" + holder.name.text.toString(), Toast.LENGTH_LONG)
                .show()
            context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone")))
        }
    }

}