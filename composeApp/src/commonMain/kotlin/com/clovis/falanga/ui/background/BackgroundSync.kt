package com.clovis.falanga.ui.background


expect object BackgroundSync {

    fun schedule(
        baseUrl: String,
        path: String = "/sync",
        intervalMinutes: Int = 15
    )

    suspend fun runOnceNow(
        baseUrl: String,
        path: String = "/sync"
    )
}
