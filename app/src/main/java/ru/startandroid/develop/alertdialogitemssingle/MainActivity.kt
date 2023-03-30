package ru.startandroid.develop.alertdialogitemssingle

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

const val LOG_TAG = "myLogs"

const val DIALOG_ITEMS = 1
const val DIALOG_ADAPTER = 2
const val DIALOG_CURSOR = 3

class MainActivity : AppCompatActivity() {

    var db: DB? = null
    var cursor: Cursor? = null

    val data = arrayOf("one", "two", "three", "four")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        db = DB(this)
        db!!.open()
        cursor = db!!.getAllData()
        startManagingCursor(cursor)
    }
    
    fun onClick(v: View) {
        when(v.id) {
            R.id.btnItems -> showDialog(DIALOG_ITEMS)
            R.id.btnAdapter -> showDialog(DIALOG_ADAPTER)
            R.id.btnCursor -> showDialog(DIALOG_CURSOR)
            else -> {}
        } 
    }

    override fun onCreateDialog(id: Int): Dialog {
        val adb: AlertDialog.Builder = AlertDialog.Builder(this)
        when(id) {
            DIALOG_ITEMS -> {
                adb.setTitle(R.string.items)
                adb.setSingleChoiceItems(data, -1, myClickListener)
            }
            DIALOG_ADAPTER -> {
                adb.setTitle(R.string.adapter)
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, data)
                adb.setSingleChoiceItems(adapter, -1, myClickListener)
            }
            DIALOG_CURSOR -> {
                adb.setTitle(R.string.cursor)
                adb.setSingleChoiceItems(cursor, -1, COLUMN_TXT, myClickListener)
            }
        }
        adb.setPositiveButton(R.string.ok, myClickListener)
        return adb.create()
    }
    
    var myClickListener: OnClickListener = object : OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val lv: ListView = (dialog as AlertDialog).listView
            if (which == Dialog.BUTTON_POSITIVE) {
                Log.d(LOG_TAG, "pos = ${lv.checkedItemCount}")
            } else {
                Log.d(LOG_TAG, "which = $which")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db!!.close()
    }

    override fun onPrepareDialog(id: Int, dialog: Dialog?) {
        (dialog as AlertDialog).listView.setItemChecked(2, true)
    }
}