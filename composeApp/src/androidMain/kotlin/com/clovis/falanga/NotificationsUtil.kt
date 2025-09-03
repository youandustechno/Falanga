package com.clovis.falanga

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import java.util.Date

object NotificationsUtils {

    private const val CHANNEL_ID="notification_channel"
    private const val CHANNEL_NAME = "Period Notifications"

    private fun setNotificationManager(context: Context): NotificationManager {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        return notificationManager
    }

    fun showCryptoNotifications(crypto: String, name: String, context: Context) {
        val notificationManager =  setNotificationManager(context)

        val notification =  NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setContentTitle(name)
            .setContentText(crypto)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .build()

        notificationManager.notify((Date().time/1000).toInt(), notification)
    }

    fun showWorkerNotifications(name: String, context: Context) {

        val notificationManager = setNotificationManager(context)
        val intentAction = Intent(context, NotificationReceiver::class.java)
        intentAction.putExtra("action", "launch")
        val pIntent = PendingIntent
            .getBroadcast(context,1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setContentTitle(name)
            .setContentText(name?:"Worker has stopped")
            .setSmallIcon(R.mipmap.ic_launcher)
            .addAction(R.mipmap.ic_launcher, "Launch manager", pIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify((Date().time/1000).toInt(), notification)
    }
}