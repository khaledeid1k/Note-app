package com.example.noteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter ():
    RecyclerView.Adapter<NotesAdapter.viewHolder>() {

      private var arr_notes: ArrayList<Notes>?=null
     private var oneClick: OnItemClickListener?=null
     private var longClick: OnItemLongClickListener?=null
     constructor(arr_notes: ArrayList<Notes>,
                 oneClick: OnItemClickListener?,
                 longClick: OnItemLongClickListener?
     ) : this() {
        this.arr_notes = arr_notes
        this.oneClick = oneClick
        this.longClick = longClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.
        from(parent.context)
            .inflate(R.layout.item_note, parent, false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        // Get the data model based on position
        val notes = arr_notes?.get(position)
      holder.title.text=notes?.title
      holder.date.text=notes?.timestamp.toString()

        holder.itemView.setOnClickListener { oneClick?.onItemClick(position) }
        holder.itemView.setOnLongClickListener { longClick?.onItemLongClick(position) ?:  false}



    }

    override fun getItemCount(): Int {
        return arr_notes?.size ?: 0
    }



    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val date = itemView.findViewById<TextView>(R.id.date)



    }


     interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
     interface OnItemLongClickListener {
        fun onItemLongClick(position: Int) : Boolean
    }


}