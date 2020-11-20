package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat


fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    status: String,
    notificationID: Int
) {
    // Create the content intent for the notification

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("fileName", messageBody)
    contentIntent.putExtra("status", status)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        notificationID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val downloadImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cloud
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(downloadImage)
        .bigLargeIcon(null)

    // Build the notification

    val builder = NotificationCompat.Builder(
        applicationContext,
        MainActivity.CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_cloud_download)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .setLargeIcon(downloadImage)
        .addAction(
            R.drawable.cloud,
            applicationContext.getString(R.string.check),
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(notificationID, builder.build())
}

