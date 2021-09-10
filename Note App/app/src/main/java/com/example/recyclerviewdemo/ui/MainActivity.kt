package com.example.recyclerviewdemo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.*
import com.example.recyclerviewdemo.databinding.ActivityMainBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors
import android.R.*
import android.view.Window
import android.view.WindowManager
import com.example.recyclerviewdemo.*
import com.example.recyclerviewdemo.adapter.NoteAdapter
import com.example.recyclerviewdemo.dto.NoteDTO
import com.example.recyclerviewdemo.repository.DBHandler
import com.example.recyclerviewdemo.ui.fragmnets.NoteDialogFragment


class MainActivity : AppCompatActivity(), NoteAdapter.OnNoteListener, NoteDialogFragment.OnDataPass {
    private lateinit var binding: ActivityMainBinding
    private var noteList = ArrayList<NoteDTO>()
    private lateinit var db: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        db = DBHandler.getInstance(this@MainActivity)!!

        noteList = db.viewNotes() as ArrayList<NoteDTO>
        binding.recycleView.adapter = NoteAdapter(noteList,this)
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recycleView.layoutManager = gridLayoutManager

        fabAnimation()

        binding.fab.setOnClickListener {
            var dialog = NoteDialogFragment()
            dialog.show(supportFragmentManager,dialog.tag)
        }
    }

    override fun onRestart() {
        super.onRestart()

        Executors.newSingleThreadExecutor().execute {
            noteList.clear()
            noteList.addAll(db.viewNotes())
        }

        (binding.recycleView.adapter as NoteAdapter).setList(noteList)

    }


    private fun fabAnimation(){
        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.fab.isShown) {
                    binding.fab.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.fab.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun onNoteClick(position: Int) {

        val intent = Intent(applicationContext, NoteActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("value", noteList[position])
        intent.putExtras(bundle)
        startActivity(intent)

        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    override fun onDataPass(data: NoteDTO?) {
        if (data != null) {
            db.addNote(data)
        }
       Executors.newSingleThreadExecutor().execute {
            noteList.clear()
            noteList.addAll(db.viewNotes())
       }

       (binding.recycleView.adapter as NoteAdapter).setList(noteList)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}