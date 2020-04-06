package com.example.democontentprovidersqlite

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.democontentprovidersqlite.model.Student
import com.example.sqlitedemo1.adapter.StudentsAdapter
import com.example.sqlitedemo1.data.DBManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_create_student.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update_student.*

class MainActivity : AppCompatActivity() {

    lateinit var arrST: ArrayList<Student>
    lateinit var dbManager: DBManager
    lateinit var studentsAdapter: StudentsAdapter
    lateinit var imageStudent: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbManager = DBManager(this)
        arrST = ArrayList()

        if (dbManager.getAllStudents().isNotEmpty()) {
            arrST = dbManager.getAllStudents()
            setAdapter()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_student -> {
                val intent = Intent(this, CreateStudentActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAdapter() {
        if (recyclerviewStudent.adapter == null) {
            studentsAdapter = StudentsAdapter(arrST)
            recyclerviewStudent.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = studentsAdapter
                addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(position: Int, view: View) {
                        showDialogUpdate(position)
                    }
                })
            }
        } else {
            recyclerviewStudent.adapter!!.notifyDataSetChanged()
        }
        addItemTouchCallback(recyclerviewStudent)
    }

    private fun addItemTouchCallback(recyclerView: RecyclerView) {
        val callback: ItemTouchHelper.Callback =
            ItemTouchHelperCallback(object : onItemTouchListenner {
                override fun onSwipe(position: Int) {
                    dbManager.deleteStudent(
                        "${DBManager.ID}=?",
                        arrayOf("${arrST[position].mID}")
                    )
                    studentsAdapter.onSwipeAdapter(position)
                    Toast.makeText(
                        this@MainActivity,
                        "Delete student success !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun showDialogUpdate(position: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_update_student)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        imageStudent = dialog.findViewById(R.id.dialogImageStudent)
        val textName: TextView = dialog.findViewById(R.id.dialogTextName)
        val textPhone: TextView = dialog.findViewById(R.id.dialogTextPhone)
        val textAddress: TextView = dialog.findViewById(R.id.dialogTextAddress)
        val textEmail: TextView = dialog.findViewById(R.id.dialogTextEmail)
        val buttonClose: Button = dialog.findViewById(R.id.dialogButtonClose)
        val buttonUpdate: Button = dialog.findViewById(R.id.dialogButtonUpdate)

        textName.text = arrST[position].mName
        textPhone.text = arrST[position].mPhone
        textAddress.text = arrST[position].mAddress
        textEmail.text = arrST[position].mEmail
        imageStudent.setImageBitmap(BitmapUtils.getImage(arrST[position].mImage))

        imageStudent.setOnClickListener {
            requestStoragePermission(this)
        }

        buttonClose.setOnClickListener {
            dialog.dismiss()
        }

        buttonUpdate.setOnClickListener {
            arrST[position].mName = textName.text.toString()
            arrST[position].mPhone = textPhone.text.toString()
            arrST[position].mAddress = textAddress.text.toString()
            arrST[position].mEmail = textEmail.text.toString()
            arrST[position].mImage = BitmapUtils.getBytes(imageStudent.drawable.toBitmap())
            dbManager.updateStudent(
                setContentValues(arrST[position]), "${DBManager.ID}=?",
                arrayOf("${arrST[position].mID}")
            )
            dialog.dismiss()
            Toast.makeText(this, "Update student success !", Toast.LENGTH_SHORT).show()
            setAdapter()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 113) {
            imageStudent.setImageURI(data!!.data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.close()
    }
}
