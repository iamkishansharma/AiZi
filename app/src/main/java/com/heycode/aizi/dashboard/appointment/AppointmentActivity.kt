package com.heycode.aizi.dashboard.appointment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.heycode.aizi.R
import com.heycode.aizi.models.AppointmentModel

class AppointmentActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    //firebase
    private lateinit var mFirestore: FirebaseFirestore

    private var appointAdapter: AppointmentRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)
        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Appointments"

        recyclerView = findViewById(R.id.appointment_recycler_view)

        //firebase
        val user = FirebaseAuth.getInstance().currentUser
        mFirestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.identify_recycler_view)

        val path = "appointments" + user!!.uid
        val query: Query = mFirestore.collection(path)
        val allAppointments: FirestoreRecyclerOptions<AppointmentModel> =
            FirestoreRecyclerOptions.Builder<AppointmentModel>()
                .setQuery(query, AppointmentModel::class.java)
                .build()

        appointAdapter = AppointmentRVAdapter(allAppointments, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = appointAdapter

    }


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
            holder.title.text = model.name
            holder.address.text = model.name
            holder.work.text = model.name
            holder.time.text = model.name
            holder.date.text = model.name

//            holder.itemView.findViewById<ImageButton>(R.id.item_call).setOnClickListener {
//                val phone: String = model.address
//                Toast.makeText(
//                    context,
//                    "Calling...\n" + holder.name.text.toString(),
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//                context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone")))
//            }
        }
    }
}