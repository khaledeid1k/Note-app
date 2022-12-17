package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteapp.databinding.ActivityBodyNoteBinding

class BodyNode : AppCompatActivity() {
    lateinit var binding : ActivityBodyNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBodyNoteBinding.inflate(layoutInflater)
        setContentView(  binding.root)
        binding.titleOfNote.text= intent.extras?.getString("title")
        binding.bodyOfNote.text= intent.extras?.getString("body")
    }
}