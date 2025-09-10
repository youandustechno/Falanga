package com.clovis.falanga.ui.background

actual object BackgroundSync {
    actual fun schedule(baseUrl: String, path: String, intervalMinutes: Int) {
    }

    actual suspend fun runOnceNow(baseUrl: String, path: String) {
    }
}