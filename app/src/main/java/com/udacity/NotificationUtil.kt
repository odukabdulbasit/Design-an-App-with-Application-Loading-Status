package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, downloadID: Long, status: String, url: String) {
    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
        .putExtra("downloadID", downloadID)
        .putExtra("status", status)
        .putExtra("url", url)

    val detailPendingIntent= PendingIntent.getActivity(applicationContext, NOTIFICATION_ID,detailIntent,
        PendingIntent.FLAG_UPDATE_CURRENT)

    val bigTextStyle = NotificationCompat.BigTextStyle().bigText(status)

    val builder= NotificationCompat.Builder(applicationContext,applicationContext.getString(R.string.details_channel_id))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(detailPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigTextStyle)
        .addAction(R.drawable.ic_assistant_black_24dp, applicationContext.getString(R.string.download_details), detailPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_MAX)

    notify(NOTIFICATION_ID, builder.build())
}