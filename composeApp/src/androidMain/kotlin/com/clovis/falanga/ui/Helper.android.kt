package com.clovis.falanga.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getContext(): Any {
    return LocalContext.current
}