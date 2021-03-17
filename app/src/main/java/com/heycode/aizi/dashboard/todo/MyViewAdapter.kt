package com.heycode.aizi.dashboard.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heycode.aizi.R


class MyViewAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<MyViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.textView_title)
        var description: TextView = itemView.findViewById(R.id.textView_description)
        var taskCompleted: CheckBox = itemView.findViewById(R.id.task_completed)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position]
        holder.description.text = items[position]
        holder.taskCompleted.isChecked = false
    }

    override fun getItemCount(): Int {
        return items.size
    }
}