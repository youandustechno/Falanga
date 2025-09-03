package com.clovis.falanga

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("action")
        if (message.equals("launch")) {
            Intent().also { intents ->
                intents.component = ComponentName(
                    "com.clovis.falanga",
                    "com.clovis.falanga.MainActivity"
                )
                intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intents)
            }
        }
    }
}