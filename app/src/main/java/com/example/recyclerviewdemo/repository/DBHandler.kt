package com.example.recyclerviewdemo.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.recyclerviewdemo.dto.NoteDTO

class DBHandler private constructor(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "NotesDatabase"
        private val TABLE_NOTES = "NotesTable"
        // Columns
        private val KEY_ID ="id"
        private val KEY_TITLE = "title"
        private val KEY_CONTENT = "content"
        private val KEY_Category = "category"
        private  val KEY_LEVEL = "level"

        private var sInstance: DBHandler? = null

        @Synchronized
        fun getInstance(context: Context): DBHandler? {
            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
            if (sInstance == null) {
                sInstance = DBHandler(context.applicationContext)
            }
            return sInstance
        }

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_NOTES_TABLE = ("CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY ," + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT," + KEY_Category + " TEXT," + KEY_LEVEL + " TEXT" + ")")
        db?.execSQL(CREATE_NOTES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES)
            onCreate(db)
        }
    }

    //method to insert data
    fun addNote(note: NoteDTO):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_CONTENT, note.content)
        contentValues.put(KEY_Category, note.category)
        contentValues.put(KEY_LEVEL,note.level )
        // Inserting Row
        val success = db.insert(TABLE_NOTES, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to read data
    @SuppressLint("Range")
    fun viewNotes():List<NoteDTO>{
        val noteList:ArrayList<NoteDTO> = ArrayList<NoteDTO>()
        val selectQuery = "SELECT  * FROM $TABLE_NOTES"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var note_id:Int
        var note_title: String
        var note_content: String
        var note_category: String
        var note_level: String
        if (cursor.moveToFirst()) {
            do {
                note_id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                note_title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                note_content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT))
                note_category = cursor.getString(cursor.getColumnIndex(KEY_Category))
                note_level = cursor.getString(cursor.getColumnIndex(KEY_LEVEL))

                val note= NoteDTO(id = note_id,title = note_title , content = note_content, category = note_category, level = note_level )
                Log.d("DB",note.title.toString())
                noteList.add(note)
            } while (cursor.moveToNext())
        }
        db.close()
        return noteList
    }
    //method to update data
    fun updateNotes(note: NoteDTO):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_CONTENT, note.content)
        contentValues.put(KEY_Category, note.category)
        contentValues.put(KEY_LEVEL,note.level )

        // Updating Row
        val success = db.update(TABLE_NOTES, contentValues, KEY_ID +"="+note.id,null)
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteNotes(note: NoteDTO):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, note.title) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_NOTES, KEY_ID +"="+note.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}