package com.clovis.falanga

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.clovis.falanga.ui.HandleBackPress
import com.clovis.falanga.ui.Routes
import com.clovis.falanga.ui.background.BackgroundSync
import com.clovis.falanga.ui.isAndroid
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(prefs: DataStore<Preferences>) {

    MaterialTheme {
        if(isAndroid()) {
            HandleBackPress()
        }

        rememberCoroutineScope().launch {
            BackgroundSync.schedule("","")
            //BackgroundSync.runOnceNow("", "")
        }
        Routes(prefs)
    }
}