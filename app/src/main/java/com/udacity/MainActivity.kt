package com.udacity


import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.NullPointerException


class MainActivity : AppCompatActivity() {

    var fileName = ""
    var check_status = "Fail"
    private var downloadID: Long = 0
    private var downloadManager: DownloadManager? = null

    private lateinit var notificationManager: NotificationManager

    private var URL = " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_name)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        custom_button.setOnClickListener {
            when {
                glide.isChecked -> {
                    URL = "https://github.com/bumptech/glide"
                    fileName = getString(R.string.glide)
                    download()
                }
                load.isChecked -> {
                    URL =
                        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                    fileName = getString(R.string.load)
                    download()
                }
                retrofit.isChecked -> {
                    URL = "https://github.com/square/retrofit"
                    fileName = getString(R.string.retrofit)
                    download()
                }
                else -> {
                    Toast.makeText(this, "Please select any option", Toast.LENGTH_LONG).show()
                        .toString()

                }
            }
        }


    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID == id) {
                val action = intent.action
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                    val query = DownloadManager.Query()
                    query.setFilterById(downloadID)
                    val c = downloadManager!!.query(query)
                    if (c.moveToFirst()) {
                        val columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS)
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)
                        ) {
                            check_status = " Success"
                        }
                    }
                }
                // download completed
                custom_button.hasCompletedDownload()
                sendNotification(downloadID.toInt())

            }

        }
    }


    private fun createChannel(channelId: String, channelName: String) {
        // create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "load the file"

            notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)


        }

    }

    private fun sendNotification(notificationId: Int) {
        notificationManager.sendNotification(
            fileName,
            this,
            check_status,
            notificationId
        )
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)


        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager!!.enqueue(request)// enqueue puts the download request in the queue.


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    companion object {

        const val CHANNEL_ID = "channelId"
    }


}
