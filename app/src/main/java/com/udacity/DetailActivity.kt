package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var status: String? = ""
    private var url: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val extras = intent.extras!!
        downloadID = extras.getLong("downloadID")
        status = extras.getString("status")
        url = extras.getString("url")
        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager
        notificationManager.cancelAll()
        val fileNameTxt: TextView = findViewById(R.id.file_name_value)
        fileNameTxt.text = url
        val statusValueTxt: TextView = findViewById(R.id.status_value)
        statusValueTxt.text = status


        fab.setOnClickListener {
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }
    }

}
