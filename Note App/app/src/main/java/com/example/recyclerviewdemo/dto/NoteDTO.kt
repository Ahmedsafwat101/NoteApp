package com.example.recyclerviewdemo.dto

import android.graphics.Color
import java.io.Serializable

data class NoteDTO(
    val id:Int,
    val title:String,
    val content:String,
    val category: String,
    val level:String
    ):Serializable