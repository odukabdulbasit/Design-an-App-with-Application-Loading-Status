package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    lateinit var downloadManager: DownloadManager

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            setUrlFromRadio()
            download()
        }

        createChannel(
            getString(R.string.details_channel_id),
            getString(R.string.details_channel_name)
        )
    }

    private fun setUrlFromRadio() {
        when {
            radioGlide.isChecked -> {
                URL = "https://github.com/bumptech/glide"
            }
            radioLoadApp.isChecked -> {
                URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
            }
            radioRetrofit.isChecked -> {
                URL = "https://github.com/square/retrofit"
            }
            else -> {
                download()
            }
        }
        custom_button.setButtonStatus(ButtonState.Clicked)
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID == id){
                Toast.makeText(
                    applicationContext,
                    "Download completed",
                    Toast.LENGTH_SHORT
                ).show();
                //sendDownloadCompleteNotification()
                custom_button.setButtonStatus(ButtonState.Completed)
                val query = DownloadManager.Query().setFilterById(id)
                val cursor = downloadManager.query(query)
                cursor.moveToFirst()
                val status = when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_SUCCESSFUL -> "Success"
                    else -> "Failed"
                }
                notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
                notificationManager.sendNotification(context?.getText(R.string.file_ready).toString(), applicationContext , id, status, URL!!)
            }
        }
    }

    private fun download() {

        custom_button.setButtonStatus(ButtonState.Loading)
        if (URL != null){

            val request =
                DownloadManager.Request(Uri.parse(URL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)
                    .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, FILE_NAME)
            downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }else// If does not select any radio button
        {
            // Show a toast to remind user to select a file
            Toast.makeText(this, "Please select a file to download", Toast.LENGTH_SHORT).show()

            // Set loading state to completed
            custom_button.setButtonStatus(ButtonState.Completed)
        }

    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.details_channel_name)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private var URL : String? = null

        private const val CHANNEL_ID = "channelId"
        private const val FILE_NAME = "LoadAppDownloaded"
    }

}
