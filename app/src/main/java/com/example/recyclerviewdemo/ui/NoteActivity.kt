package com.example.recyclerviewdemo.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recyclerviewdemo.dto.NoteDTO
import com.example.recyclerviewdemo.databinding.ActivityNoteBinding
import com.example.recyclerviewdemo.repository.DBHandler
import android.content.Intent
import com.example.recyclerviewdemo.adapter.NoteAdapter
import java.util.concurrent.Executors


class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    private lateinit var db:DBHandler
    private lateinit var currNote:NoteDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = DBHandler.getInstance(context = applicationContext)!!
        // Retrieve Object form MainActivity
        val extras = intent.extras
        if(extras!=null) {
            // Populate Note Data
             currNote = extras.getSerializable("value") as NoteDTO

            binding.noteTitle.setText(currNote.title)
            binding.noteContent.setText(currNote.content)
        }
    }

    override fun finish() {
        Executors.newSingleThreadExecutor().execute {
            db.updateNotes(
                NoteDTO(
                    id = currNote.id,
                    title = binding.noteTitle.text.toString(),
                    content = binding.noteContent.text.toString(),
                    category = currNote.category,
                    level = currNote.level
                )
            )
        }
        super.finish()
    }
}
