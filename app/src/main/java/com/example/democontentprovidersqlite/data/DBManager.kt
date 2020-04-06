package com.example.sqlitedemo1.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.democontentprovidersqlite.model.Student

class DBManager(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    private var db: SQLiteDatabase = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        val sqlquery = "CREATE TABLE students (\n" +
                "    students_id      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                             NOT NULL,\n" +
                "    students_name    STRING    NOT NULL,\n" +
                "    students_phone   STRING    NOT NULL,\n" +
                "    students_address STRING    NOT NULL,\n" +
                "    students_email   STRING    NOT NULL,\n" +
                "    students_image   BLOB    NOT NULL\n" +
                ");"
        db!!.execSQL(sqlquery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertStudent(values: ContentValues?): Long {
        return db.insert(TABLE_NAME, null, values)
    }

    fun deleteStudent(selection: String?, selectionArgs: Array<String>?): Int {
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }

    fun updateStudent(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun queryStudents(
        projection: Array<String>?,       //columns trong table vd: arrayOf("students_address")
        selection: String?,               // Điều kiện where vd: "students_id=?"
        selectionArgs: Array<String>?,    // Gía trị điều kiện where vd: arrayOf("3") có thể hiểu là students_id=3
        orderBy: String?                  // Sắp xếp
    ): Cursor {
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy)
    }

    fun getAllStudents(): ArrayList<Student> {
        val arrST = ArrayList<Student>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        cursor.apply {

        }
        if (cursor.moveToFirst()) {
            do {
                val student = Student(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getBlob(5)
                )
                arrST.add(student)
            } while (cursor.moveToNext())
        }
        return arrST
    }

    companion object {
        const val DATABASE_NAME = "db_students"
        const val TABLE_NAME = "students"
        const val ID = "students_id"
        const val NAME = "students_name"
        const val PHONE = "students_phone"
        const val ADDRESS = "students_address"
        const val EMAIL = "students_email"
        const val IMAGE = "students_image"
        const val VERSION = 1
    }
}
