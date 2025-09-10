package com.clovis.falanga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.clovis.falanga.ui.background.AppCtx
import com.clovis.falanga.ui.background.BackgroundSync

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppCtx.context = applicationContext
            App(prefs = remember { createDataSTore(applicationContext) })
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(createDataSTore(LocalContext.current))
}