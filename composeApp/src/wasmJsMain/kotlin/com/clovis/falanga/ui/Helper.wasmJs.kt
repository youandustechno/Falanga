package com.clovis.falanga.ui

import androidx.compose.runtime.Composable

actual fun isAndroid(): Boolean {
    return false
}

@Composable
actual fun getContext(): Any {
    return ""
}

@Composable
actual fun HandleBackPress() {
}