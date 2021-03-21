package com.heycode.aizi.dashboard.todo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.heycode.aizi.R

class TodoActivity : AppCompatActivity() {
    lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private var taskAdapter: TaskRecyclerViewAdapter? = null
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Tasks ToDo"

        //firebase
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        fab = findViewById(R.id.buttonAddNote)
        fab.setOnClickListener {
            startActivity(Intent(this@TodoActivity, AddTaskActivity::class.java))
        }

        //RecyclerView setup from firebaseUI
        val query: Query =
            mFirestore.collection(user!!.uid).orderBy("title", Query.Direction.DESCENDING)

        val allTasks: FirestoreRecyclerOptions<Task> =
            FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task::class.java)
                .build()

        taskAdapter = TaskRecyclerViewAdapter(allTasks, this)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = taskAdapter


        //RecyclerView is Scrolled then Hide Fab Button
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility != View.VISIBLE) {
                    fab.show()
                } else {
                    fab.show()
                }
            }
        })

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val docId: String = taskAdapter!!.snapshots.getSnapshot(viewHolder.position).id

                val reference: DocumentReference =
                    FirebaseFirestore.getInstance().collection("${user?.uid}").document(docId)

                reference.delete()

                val unDo: ArrayList<Task> = ArrayList()
                unDo.add(taskAdapter!!.getItem(viewHolder.adapterPosition))

                val snackbar = Snackbar.make(
                    findViewById(R.id.coordinateLayout),
                    "Deleted: " + taskAdapter!!.getItem(viewHolder.adapterPosition).title,
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Undo") {

                        val reference: DocumentReference =
                            FirebaseFirestore.getInstance().collection("${user?.uid}").document()

                        val data = HashMap<String, Any>()
                        data["title"] = unDo[0].title.toString()
                        data["description"] = unDo[0].description.toString()
                        data["completed"] = unDo[0].completed.toString()
                        reference.set(data)
                        unDo.clear()
                    }
                snackbar.setActionTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                snackbar.show()
            }
        }).attachToRecyclerView(recyclerView)

    }


    override fun onResume() {
        super.onResume()
        // Starts the FAB animation
        val handler = Handler()
        handler.postDelayed({
            val animation = AnimationUtils.loadAnimation(this, R.anim.fab_animation)
            val interpolator: Interpolator = BounceInterpolator()
            animation.interpolator = interpolator
            fab.startAnimation(animation)
            fab.visibility = View.VISIBLE
        }, 300)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all -> {
                //Taking permission to delete all notes
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Deleting all your notes.")
                    .setCancelable(false)
                    .setMessage("Are you sure?")
                    .setIcon(R.drawable.logo)
                    .setPositiveButton(
                        "Yes"
                    ) { _, _ ->
                        if (recyclerView.adapter!!.itemCount == 0) {
                            Toast.makeText(
                                this,
                                "Nothing to Delete",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            var i = 0
                            while (i < taskAdapter!!.itemCount) {
                                FirebaseFirestore.getInstance().collection("${user?.uid}")
                                    .document(taskAdapter!!.snapshots.getSnapshot(i).id)
                                    .delete()
                                i++
                            }
                            val snackbar: Snackbar = Snackbar.make(
                                findViewById(R.id.coordinateLayout),
                                "All your Tasks deleted!",
                                Snackbar.LENGTH_LONG
                            )
                            snackbar.show()
                        }
                    }
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss() })
                val dialog = builder.create()
                dialog.show()
                true
            }
            R.id.settings -> {
//                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        taskAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        taskAdapter!!.stopListening()
    }

    override fun onPostResume() {
        super.onPostResume()
        taskAdapter!!.startListening()
    }

}
