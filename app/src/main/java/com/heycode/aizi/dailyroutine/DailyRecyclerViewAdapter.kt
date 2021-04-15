package com.heycode.aizi.dailyroutine

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.util.*
import kotlin.collections.HashMap


class DailyRecyclerViewAdapter(
    options: FirestoreRecyclerOptions<RoutineModel>,
    var context: Context
) :
    FirestoreRecyclerAdapter<RoutineModel, DailyRecyclerViewAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.routine_title)
        var date: TextView = itemView.findViewById(R.id.routine_date)
        var clockImage: ImageButton = itemView.findViewById(R.id.clock_image)
        var time: TextView = itemView.findViewById(R.id.routine_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.routine_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: RoutineModel) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 10, intent, 0)

        holder.title.text = model.title
        holder.date.text = model.date
        holder.time.text = model.time
        if (model.activeAlarm.equals("yes")) {
            holder.clockImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_alarm))
        } else {
            holder.clockImage.alpha = 0.2F
        }

        holder.itemView.findViewById<ImageButton>(R.id.clock_image).setOnClickListener {
            val docId: String = snapshots.getSnapshot(position).id

            val user = FirebaseAuth.getInstance().currentUser
            val reference: DocumentReference =
                FirebaseFirestore.getInstance().collection("routines${user?.uid}").document(docId)

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
                Log.d("DailyAlarm", "Completed!")
            }.addOnFailureListener {
                Log.d("DailyAlarm", "Failed!")
            }
            notifyDataSetChanged()
        }
    }
}