package com.heycode.aizi.dashboard.medicine

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.heycode.aizi.R
import com.heycode.aizi.dailyroutine.AlarmReceiver
import com.heycode.aizi.models.MedicineModel
import java.util.*
import kotlin.collections.HashMap


class MedicineRVAdapter(options: FirestoreRecyclerOptions<MedicineModel>, var context: Context) :
    FirestoreRecyclerAdapter<MedicineModel, MedicineRVAdapter.MyViewHolder>(options) {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.medicine_image)
        var title: TextView = itemView.findViewById(R.id.medicine_title)
        var time: TextView = itemView.findViewById(R.id.medicine_time)
        var description: TextView = itemView.findViewById(R.id.medicine_description)
        var alarmClock: ImageButton = itemView.findViewById(R.id.medicine_clock_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_medicine, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: MedicineModel) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, position, intent, 0)

        Glide.with(context)
            .load(model.image)
            .centerCrop()
            .into(holder.imageView)

        holder.title.text = model.title
        holder.description.text = model.description
        holder.time.text = model.time

        if (model.activeAlarm.equals("yes")) {
            holder.alarmClock.setImageDrawable(context.resources.getDrawable(R.drawable.ic_alarm))
            holder.alarmClock.alpha = 1.0F
        } else {
            holder.alarmClock.alpha = 0.2F
        }

        holder.itemView.findViewById<ImageButton>(R.id.medicine_clock_image).setOnClickListener {

            val docId: String = snapshots.getSnapshot(position).id

            val user = FirebaseAuth.getInstance().currentUser
            val reference: DocumentReference =
                FirebaseFirestore.getInstance().collection("medicines${user?.uid}").document(docId)

            val data = HashMap<String, Any>()
            if (model.activeAlarm == "no") {
                data["activeAlarm"] = "yes"

                val time = holder.time.text.split(":").toTypedArray()
                val cal: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, time[0].toInt())
                    set(Calendar.MINUTE, time[1].toInt())
                }
                Toast.makeText(
                    context,
                    "Alarm activated!",
                    Toast.LENGTH_SHORT
                ).show()

                alarmMgr?.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    cal.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

            } else {
                data["activeAlarm"] = "no"
                alarmMgr?.cancel(pendingIntent)
                AlarmReceiver.stopMedia()
            }

            reference.update(data).addOnSuccessListener {
                Log.d("DailyAlarmForMedicine", "Completed!")
            }.addOnFailureListener {
                Log.d("DailyAlarm", "Failed!")
            }
            notifyDataSetChanged()
        }
    }
}