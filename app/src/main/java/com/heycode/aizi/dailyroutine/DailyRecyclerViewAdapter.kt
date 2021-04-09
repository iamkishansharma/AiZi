package com.heycode.aizi.dailyroutine

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.heycode.aizi.R
import com.heycode.aizi.models.RoutineModel


class DailyRecyclerViewAdapter(
    options: FirestoreRecyclerOptions<RoutineModel>,
    var context: Context
) :
    FirestoreRecyclerAdapter<RoutineModel, DailyRecyclerViewAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.routine_title)
        var date: TextView = itemView.findViewById(R.id.routine_date)
        var clockImage: ImageButton = itemView.findViewById(R.id.clock_image)
        var time: CheckBox = itemView.findViewById(R.id.routine_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.routine_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: RoutineModel) {
        holder.title.text = model.title
        holder.date.text = model.date
        holder.time.text = model.time
        holder.clockImage.isEnabled = model.activeAlarm.equals("yes")

        holder.itemView.findViewById<CheckBox>(R.id.clock_image).setOnClickListener {
            val docId: String = snapshots.getSnapshot(position).id

            val user = FirebaseAuth.getInstance().currentUser
            val reference: DocumentReference =
                FirebaseFirestore.getInstance().collection("${user?.uid}").document(docId)

            val data = HashMap<String, Any>()
            if (model.activeAlarm == "no") {
                data["completed"] = "yes"
                Toast.makeText(context, "Alarm activated !", Toast.LENGTH_SHORT).show()
            } else {
                data["completed"] = "no"
            }

            reference.update(data).addOnSuccessListener {
                Log.d("DailyAlarm", "Completed!")
            }.addOnFailureListener {
                Log.d("DailyAlarm", "Failed!")
            }
            notifyDataSetChanged()
        }
    }
}