package com.example.grocerygo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GroceryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "grocery.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allgroceries"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)

    }

    fun insertGrocery(grocery: Grocery): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, grocery.title)
            put(COLUMN_CONTENT, grocery.content)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L // Return true if insertion was successful, false otherwise
    }


    fun getAllGroceries(): List<Grocery> {
        val groceryList = mutableListOf<Grocery>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            // Create Grocery object and add it to the list
            val grocery = Grocery(id, title, content)
            groceryList.add(grocery)
        }
        cursor.close()
        db.close()
        return groceryList
    }

    fun updateGrocery(grocery: Grocery){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE,grocery.title)
            put(COLUMN_CONTENT,grocery.content )

        }

        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(grocery.id.toString())
        db.update(TABLE_NAME,values,whereClause,whereArgs)
        db.close()
    }

    fun getGroceryByID(groceryId: Int):Grocery{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $groceryId"
        val curser = db.rawQuery(query,null)
        curser.moveToFirst()

        val id = curser.getInt(curser.getColumnIndexOrThrow(COLUMN_ID))
        val title = curser.getString(curser.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = curser.getString(curser.getColumnIndexOrThrow(COLUMN_CONTENT))

        curser.close()
        db.close()
        return Grocery(id,title,content)
    }

    fun deleteGrocery(groceryId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(groceryId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }



}

