package com.clovis.falanga.ui.background

// iosMain/kotlin/background/BackgroundSync.ios.kt

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.BackgroundTasks.*
import platform.Foundation.*
import platform.darwin.*
import kotlinx.cinterop.*
import kotlin.coroutines.resume

private const val TASK_ID = "com.yourapp.refresh"

private var isRegistered = false

private fun registerIfNeeded() {
    if (isRegistered) return
    isRegistered = true

    // register(forTaskWithIdentifier:using:launchHandler:)
    BGTaskScheduler.sharedScheduler().registerForTaskWithIdentifier(
        identifier = TASK_ID,
        usingQueue = null
    ) { task ->
        val refresh = task as BGAppRefreshTask

        // reschedule first
        scheduleIn(seconds = 30.0 * 60.0)

        val defaults = NSUserDefaults.standardUserDefaults
        val baseUrl = defaults.stringForKey("sync.baseUrl") ?: run {
            refresh.setTaskCompletedWithSuccess(false); return@registerForTaskWithIdentifier
        }
        val path = defaults.stringForKey("sync.path") ?: "/sync"
        val urlStr = (if (baseUrl.endsWith("/")) baseUrl.dropLast(1) else baseUrl) + path
        val url = NSURL(string = urlStr)
        if (url == null) { refresh.setTaskCompletedWithSuccess(false); return@registerForTaskWithIdentifier }

        val req = NSMutableURLRequest.requestWithURL(url).apply {
            setHTTPMethod("GET")
            setValue("application/json", forHTTPHeaderField = "Accept")
        }

        val session = NSURLSession.sharedSession()
        val dataTask = session.dataTaskWithRequest(req) { _, response, error ->
            val ok = (error == null) && ((response as? NSHTTPURLResponse)?.statusCode?.let { it in 200..299 } == true)
            refresh.setTaskCompletedWithSuccess(ok)
        }

        refresh.expirationHandler = { dataTask.cancel() }
        dataTask.resume()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun scheduleIn(seconds: Double) {
    memScoped {
        val req = BGAppRefreshTaskRequest(identifier = TASK_ID).apply {
            earliestBeginDate = NSDate.dateWithTimeIntervalSinceNow(seconds)
        }
        val err = alloc<ObjCObjectVar<NSError?>>()
        BGTaskScheduler.sharedScheduler().submitTaskRequest(req, err.ptr)
        // (Optional) log err.value if needed
    }
}


actual object BackgroundSync {

    actual fun schedule(baseUrl: String, path: String, intervalMinutes: Int) {
        // Persist so the BG handler can read them
        val d = NSUserDefaults.standardUserDefaults
        d.setObject(baseUrl, forKey = "sync.baseUrl")
        d.setObject(path, forKey = "sync.path")

        registerIfNeeded()
        scheduleIn(seconds = intervalMinutes.toDouble() * 60.0) // iOS decides actual timing
    }

    actual suspend fun runOnceNow(baseUrl: String, path: String): Unit =
        suspendCancellableCoroutine { cont ->
            val urlStr = (if (baseUrl.endsWith("/")) baseUrl.dropLast(1) else baseUrl) + path
            val url = NSURL(string = urlStr)
            if (url == null) { cont.resume(Unit); return@suspendCancellableCoroutine }

            val req = NSMutableURLRequest.requestWithURL(url).apply {
                setHTTPMethod("GET")
                setValue("application/json", forHTTPHeaderField = "Accept")
            }

            val task = NSURLSession.sharedSession().dataTaskWithRequest(req) { _, resp, err ->
                // you can return a Boolean if you like; keeping Unit for parity with expect
                cont.resume(Unit)
            }

            cont.invokeOnCancellation { task.cancel() }
            task.resume()
        }
}

