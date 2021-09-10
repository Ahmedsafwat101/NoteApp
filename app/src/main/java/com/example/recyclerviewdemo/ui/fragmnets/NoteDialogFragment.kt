package com.example.recyclerviewdemo.ui.fragmnets

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.recyclerviewdemo.databinding.NoteAddDialogBinding
import com.example.recyclerviewdemo.dto.NoteDTO
import com.example.recyclerviewdemo.repository.DBHandler


class NoteDialogFragment:DialogFragment() {


    private lateinit var db: DBHandler
    private lateinit var _binding: NoteAddDialogBinding
    private lateinit var dataPasser: OnDataPass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = NoteAddDialogBinding.inflate(inflater, container, false)
        val view = _binding.root

        db = DBHandler.getInstance(view.context)!!

        _binding.addNoteBtn.setOnClickListener {

            val selectID = _binding.noteLevels.checkedRadioButtonId
            val radio = selectID.let { it1 -> view.findViewById<RadioButton>(it1) }
            var result = radio?.text.toString()


            // Validation
            if(_binding.noteTitleTxt.text.toString().isNotBlank() &&
                _binding.noteCategoryTxt.text.toString().isNotBlank()&&
                _binding.noteContentTxt.text.toString().isNotBlank()&& result!=null) {

               //Add Note then pass It to the MainActivity
                val noteData = NoteDTO(
                    0,
                    title = _binding.noteTitleTxt.text.toString(),
                    content = _binding.noteContentTxt.text.toString(),
                    category = _binding.noteCategoryTxt.text.toString(),
                    level = result
                )

                dataPasser.onDataPass(noteData)

                Toast.makeText(context,"Note is Added",Toast.LENGTH_SHORT).show()
                dismiss()

            }else{
                Toast.makeText(context,"Please fill all fields ",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    interface OnDataPass {
        fun onDataPass(data: NoteDTO?)
    }

    fun passData(data: NoteDTO){
        dataPasser?.onDataPass(data)
    }


}