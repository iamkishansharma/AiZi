package com.heycode.aizi.dashboard.identify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.heycode.aizi.R

class IdentifyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: ExtendedFloatingActionButton

    //firebase
    private lateinit var mFirestore: FirebaseFirestore

    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStorage: FirebaseStorage
    private var peopleAdapter: PeopleRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Identify People"

        //firebase
        val user = FirebaseAuth.getInstance().currentUser
        mFirestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.identify_recycler_view)
        fab = findViewById(R.id.identify_fab)

        fab.setOnClickListener {
            startActivity(Intent(this, AddImageActivity::class.java))
        }

        val path = "peoples" + user!!.uid
        val query: Query = mFirestore.collection(path)
        val allPeoples: FirestoreRecyclerOptions<PeopleModel> =
            FirestoreRecyclerOptions.Builder<PeopleModel>()
                .setQuery(query, PeopleModel::class.java)
                .build()

        checkForAccess(path)

        peopleAdapter = PeopleRVAdapter(allPeoples, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = peopleAdapter
    }

    private fun checkForAccess(path: String) {
        if (mFirestore.collection(path).id.isEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Not allowed to use this feature!")
                .setCancelable(false)
                .setMessage("To get access send an email to ADMIN including Person's Image, Name and Phone No.")
                .setIcon(R.drawable.logo)
                .setPositiveButton(
                    "Send Mail"
                ) { _, _ ->

                    Toast.makeText(
                        this,
                        "Opening in GMail",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_EMAIL, arrayOf("heycodeinc@gmail.com"))
                        .setType("text/html")
                        .setPackage("com.google.android.gm")
                        .putExtra(
                            Intent.EXTRA_SUBJECT,
                            "Register me for Identify Feature in AiZi"
                        )
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Kindly provide me access to Identify people feature in AiZi app." +
                                " Here, I have attached Person's name, image and phone number."
                    )
                    finish()
                    startActivity(intent)
                }
                .setNegativeButton(
                    "Back"
                ) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    finish()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onStart() {
        super.onStart()
        peopleAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        peopleAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        peopleAdapter!!.stopListening()
    }

}