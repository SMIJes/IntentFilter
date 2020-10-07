package com.example.intentfilter

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import com.google.android.gms.actions.NoteIntents
import kotlinx.android.synthetic.main.activity_main.*

const val REQUEST_SELECT_CONTACT =1;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Receives intent to create a note
        if(intent.action== NoteIntents.ACTION_CREATE_NOTE && intent.type=="text/plain"){
            val displayText = "ACTION_CREATE_NOTE received:\n " +
                    intent.getStringExtra(NoteIntents.EXTRA_TEXT)
            textView1.text =   displayText

        //Receives intent to open a particular web address
        }else if(intent.action==Intent.ACTION_VIEW) {
            val displayText = "ACTION_VIEW received an intent to browse:\n " +
                    "${intent.data?.host}"
            textView1.text = displayText
        }

        //Sends intent to get a contacts
        button1.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            }
            if(intent.resolveActivity(packageManager)!= null)
                startActivityForResult(intent, REQUEST_SELECT_CONTACT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== REQUEST_SELECT_CONTACT && resultCode== RESULT_OK){
            val contactUri: Uri = data?.data ?:return
            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

            contentResolver.query(contactUri, projection, null, null,
                null).use {
                if(it!!.moveToFirst()){
                    val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val number = it.getString(numberIndex)
                    textView2.text = number.toString()
                }
            }
        }
    }
}