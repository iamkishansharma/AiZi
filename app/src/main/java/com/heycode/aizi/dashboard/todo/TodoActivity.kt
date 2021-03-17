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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.heycode.aizi.R

class TodoActivity : AppCompatActivity() {
    lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    private val animals: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Tasks ToDo"

        fab = findViewById(R.id.buttonAddNote)
        fab.setOnClickListener {
            startActivity(Intent(this@TodoActivity, AddTaskActivity::class.java))
        }

        addAnimals()
        val adapter = MyViewAdapter(animals, this)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

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
//                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
//                val unDo: ArrayList<Note> = ArrayList<Note>()
//                unDo.add(adapter.getNoteAt(viewHolder.adapterPosition)) //For BackUp
//                val snackbar = Snackbar.make(
//                    findViewById(R.id.coordinateLayout),
//                    "Deleted: " + adapter.getNoteAt(viewHolder.adapterPosition).getTitle(),
//                    Snackbar.LENGTH_LONG
//                )
//                    .setAction("Undo") {
//                        //TODO:: WRITE CODE TO UNDO deleted note
//                        noteViewModel.insert(unDo[0])
//                        unDo.removeAt(0)
//                    }
//                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent, null))
//                snackbar.show()
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
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Deleting all your notes.")
                    .setCancelable(false)
                    .setMessage("Are you sure?")
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { _, _ ->
                            if (recyclerView.adapter!!.itemCount == 0) {
                                Toast.makeText(
                                    this,
                                    "Nothing to Delete",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                //Taking permission to delete all notes
//                                noteViewModel.deleteAllNotes()
                                val snackbar: Snackbar = Snackbar.make(
                                    findViewById(R.id.coordinateLayout),
                                    "All your Tasks deleted!",
                                    Snackbar.LENGTH_LONG
                                )
                                snackbar.show()
                            }
                        })
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

    /////////////////////////////////////////
    fun addAnimals() {
        animals.add("dog")
        animals.add("cat")
        animals.add("owl")
        animals.add("cheetah")
        animals.add("raccoon")
        animals.add("bird")
        animals.add("snake")
        animals.add("lizard")
        animals.add("hamster")
        animals.add("bear")
        animals.add("lion")
        animals.add("tiger")
        animals.add("horse")
        animals.add("frog")
        animals.add("fish")
        animals.add("shark")
        animals.add("turtle")
        animals.add("elephant")
        animals.add("cow")
        animals.add("beaver")
        animals.add("bison")
        animals.add("porcupine")
        animals.add("rat")
        animals.add("mouse")
        animals.add("goose")
        animals.add("deer")
        animals.add("fox")
        animals.add("moose")
        animals.add("buffalo")
        animals.add("monkey")
        animals.add("penguin")
        animals.add("parrot")
    }
}
