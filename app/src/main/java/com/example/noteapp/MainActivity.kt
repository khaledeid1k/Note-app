package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ActivityMainBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.log

class MainActivity : AppCompatActivity() , NotesAdapter.OnItemClickListener
, NotesAdapter.OnItemLongClickListener{
    lateinit var binding :ActivityMainBinding
      private lateinit  var  firebase : DatabaseReference
      private lateinit  var  adapter_notes: NotesAdapter
     lateinit var arr:ArrayList<Notes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fireBase
        val database = FirebaseDatabase.getInstance()
        firebase = database.getReference("Notes")
        //Array of notes
        arr = ArrayList()

        //floating button to add new note
        binding.addNewNote.setOnClickListener {
            dialogAddNewNote()
        }

    }

    override fun onStart() {
        super.onStart()
        getDataFromFireBase()
    }

    //show Dialog to add new note
     private fun dialogAddNewNote(){
        val alertBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_note, null)
         alertBuilder.setView(view)
         val alertDialog = alertBuilder.create()
         alertDialog.show()
         view.findViewById<Button>(R.id.add).setOnClickListener{
             val title = view.findViewById<EditText>(R.id.title_Note).text.toString()
             val body = view.findViewById<EditText>(R.id.body_Note).text.toString()
             when {
                title.isEmpty() ->
                     Toast.makeText(this, "Pleas, put title to your note ", Toast.LENGTH_SHORT)
                         .show()

                body.isEmpty()->
                     Toast.makeText(this, "Pleas, Write your note ", Toast.LENGTH_SHORT).show()
                else ->{
                    // add data to fireBase
                    val id = firebase.push().key
                    val mynote=Notes(id!!,title,body,getCurrentDate())
                    firebase.child(id).setValue(mynote)
                    alertDialog.dismiss()
                }


             }
             }
    }

    // Method for get current date
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val mdFormat = SimpleDateFormat("EEEE hh:mm a ")
        return mdFormat.format(calendar.time)

    }

    // get data from fireBase
    private fun getDataFromFireBase(){
        firebase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //prevent repeate data in recycle view
                arr.clear()
                //add data to ArrayList
                for (n in snapshot.children){
                    val note=n.getValue(Notes::class.java)
                    arr.add(0,note!!)
                }

                //add ArrayList to adapter
                adapter_notes=NotesAdapter(arr,this@MainActivity,this@MainActivity)
                binding.recycleNotes.layoutManager =  LinearLayoutManager(applicationContext);
                binding.recycleNotes.adapter=adapter_notes

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    //Move to Next Page to view Note
    override fun onItemClick(position: Int) {
        val itemOfNote = arr.get(position)
        val intent = Intent(this, BodyNode::class.java)
        intent.putExtra("title",itemOfNote.title)
        intent.putExtra("body",itemOfNote.body)
        startActivity(intent)

    }

    override fun onItemLongClick(position: Int): Boolean {
        // view Dialog to update or Delete Note
         val view = layoutInflater.inflate(R.layout.change_note, null)
        val  alertDialog = AlertDialog.Builder(this).setView(view).create()
        alertDialog.show()
        //to set value of title in EditText to change it
        view.findViewById<EditText>(R.id.title_Note_change)
            .setText(arr[position].title.toString())

        //to set value of Body in EditText to change it
        view.findViewById<EditText>(R.id.body_Note_change)
            .setText(arr[position].body.toString())

            //get id for child of item i long click on it
        val child = firebase.child(arr[position].id!!)

        // to update note
        view.findViewById<Button>(R.id.updateNote).setOnClickListener{
            child.setValue(
            Notes(arr[position].id!!,
                view.findViewById<EditText>(R.id.title_Note_change).text.toString(),
                view.findViewById<EditText>(R.id.body_Note_change).text.toString(),
                getCurrentDate()
            )
            )

            alertDialog.dismiss()
        }

        // to Delete note
        view.findViewById<Button>(R.id.deleteNote).setOnClickListener{
            child.removeValue()
            alertDialog.dismiss()
        }

        return  false
    }


}