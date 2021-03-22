package com.heycode.aizi.dashboard.identify

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.heycode.aizi.R

class IdentifyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: ExtendedFloatingActionButton
    private lateinit var coordinatorLayout: CoordinatorLayout


    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStorage: FirebaseStorage
    private lateinit var mDatabaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Identify People"

        recyclerView = findViewById(R.id.identify_recycler_view)
        fab = findViewById(R.id.identify_fab)
        mAuth = FirebaseAuth.getInstance()
        mStorage = FirebaseStorage.getInstance()

        fab.setOnClickListener {
            startActivity(Intent(this, AddImageActivity::class.java))
        }
        val adapter = MyRecyclerViewAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    class MyRecyclerViewAdapter(var context: Context) :
        RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageNote: TextView = itemView.findViewById(R.id.item_text_view)
            var image: ImageView = itemView.findViewById(R.id.item_image_view)
            var imageCall: ImageView = itemView.findViewById(R.id.item_call)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.image.setImageDrawable(context.resources.getDrawable(R.drawable.female_doctor))
            holder.imageNote.text = "My Doctor"
            holder.imageCall.setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:9845852024")))
                Toast.makeText(
                    context,
                    "Calling " + holder.imageNote.text.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun getItemCount(): Int {
            return 10
        }


    }
}