package com.example.recyclerviewdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdemo.R
import com.example.recyclerviewdemo.databinding.ItemNoteBinding
import com.example.recyclerviewdemo.dto.NoteDTO
import com.example.recyclerviewdemo.repository.DBHandler
import java.util.concurrent.Executors

class NoteAdapter(private var mNote:ArrayList<NoteDTO>, private val onNoteListener: OnNoteListener): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var db: DBHandler?=null

    inner class ViewHolder(val binding: ItemNoteBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        db = DBHandler.getInstance(parent.context)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(mNote[position]){
                binding.noteTitle.text = this.title
                binding.noteCategory.text = this.category
                binding.noteContent.text = this.content
               var priorityColor =  when(this.level){
                    "High"-> R.color.red
                    "Medium"-> R.color.yellow
                    else -> R.color.green
                }
                binding.noteCategory.setChipBackgroundColorResource(priorityColor)

                binding.cardHolder.setOnClickListener{
                        onNoteListener.onNoteClick(position)
                }

                binding.removeNoteBtn.setOnClickListener{
                    db?.let {
                        Executors.newSingleThreadExecutor().execute {
                            it.deleteNotes(this)
                        }
                        mNote.remove(this)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mNote.size
    }

    interface OnNoteListener{
      fun onNoteClick(position: Int)
    }


    fun setList(newList:ArrayList<NoteDTO> ){
        mNote = newList
        notifyDataSetChanged()
    }
}