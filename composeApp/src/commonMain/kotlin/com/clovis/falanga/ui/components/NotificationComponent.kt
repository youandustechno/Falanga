package com.clovis.falanga.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun NotificationPermissionScreen(isPermitted: Boolean, isGranted: Boolean, request: () -> Unit) {
    if (isPermitted) {
//        val notificationPermissionState = rememberPermissionState(
//            permission = Manifest.permission.POST_NOTIFICATIONS
//        )

        // Check if permission is already granted
        if (isGranted) {
            Text("Permission granted!")
        } else {
            Column {
                Text("Permission required for notifications.")
                Button(onClick = {
                    request()
                    //notificationPermissionState.launchPermissionRequest()
                }) {
                    Text("Request Permission")
                }
            }
        }
    } else {
        Text("No permission required for your Android version.")
    }
}