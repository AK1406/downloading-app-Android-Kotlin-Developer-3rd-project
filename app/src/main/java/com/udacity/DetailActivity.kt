package com.udacity


import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private var  file = ""
    private var cStatus = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        title = "DetailActivity"


        okBtn.setOnClickListener{
          val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        file = intent.getStringExtra("fileName").toString()
        cStatus = intent.getStringExtra("status").toString()
        file_name.text = file
        status.text = cStatus

        val notificationManager =
                ContextCompat.getSystemService(
                    application,
                    NotificationManager::class.java
                ) as NotificationManager
        notificationManager.cancelNotifications()


    }
}
