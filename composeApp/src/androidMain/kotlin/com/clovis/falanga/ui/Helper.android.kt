package com.clovis.falanga.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getContext(): Any {
    return LocalContext.current
}

actual fun isAndroid(): Boolean {
    return true
}

@Composable
actual fun HandleBackPress() {
    val context = LocalContext.current
    BackHandler(enabled = true) {
        Toast.makeText(context, "Back", Toast.LENGTH_LONG).show()
    }
}