package com.example.democontentprovidersqlite

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.democontentprovidersqlite.model.Student
import com.example.sqlitedemo1.data.DBManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_create_student.*

class CreateStudentActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_student)

        dbManager = DBManager(this)

        imageViewStudent.setOnClickListener {
            requestStoragePermission(this)
        }

        buttonReset.setOnClickListener {
            resetEditText()
        }

        buttonCreate.setOnClickListener {
            dbManager.insertStudent(setContentValues(getEditText()))
            Toast.makeText(this, "Insert student success !", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun resetEditText() {
        editTextName.text = null
        editTextPhone.text = null
        editTextAddress.text = null
        editTextEmail.text = null
        imageViewStudent.setImageResource(R.drawable.photo)
    }

    private fun getEditText(): Student {
        return Student(
            editTextName.text.toString(),
            editTextPhone.text.toString(),
            editTextAddress.text.toString(),
            editTextEmail.text.toString(),
            BitmapUtils.getBytes(imageViewStudent.drawable.toBitmap())
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 113) {
            imageViewStudent.setImageURI(data!!.data)
        }
    }
}
