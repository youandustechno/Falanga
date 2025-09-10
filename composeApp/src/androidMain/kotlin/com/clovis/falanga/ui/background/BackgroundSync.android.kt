package com.clovis.falanga.ui.background

// androidMain/kotlin/background/BackgroundSync.android.kt

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.work.*
import com.clovis.falanga.NotificationsUtils
import com.clovis.falanga.PreferenceAndroid
import com.clovis.falanga.WatchedShares
import com.clovis.falanga.models.CryptoApiClient
import com.clovis.falanga.models.CryptoRepository
import com.clovis.falanga.models.platformHttpClient
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import kotlin.text.ifEmpty

// --- Simple app context holder (init once in Application.onCreate)
internal object AppCtx {
    lateinit var context: Context
    fun init(context: Context) { AppCtx.context = context.applicationContext }
}

class SyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    private var thisContext = appContext

    override suspend fun doWork(): Result = coroutineScope {
        try {
            // Make your HTTP request here
            makeHttpCalls(thisContext) {
                if(it) Result.retry()
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }


    private suspend fun makeHttpCalls(context: Context, shouldRetry:(Boolean) -> Unit){

        val httpClient = platformHttpClient()
        val api = CryptoApiClient(client = httpClient)
        val repo = CryptoRepository(api)

        var currentCryptoBuyAt: Double
        var currentCryptoSellAt: Double
        val pref = PreferenceAndroid(context)
        var notificationContent: String = "Monitoring crypto prices ..."

        val listOfWatchedShares = pref.getWatchedShares()
        var currencyToCall: WatchedShares? = listOfWatchedShares.find { !it.called }
        if(currencyToCall == null) {
            listOfWatchedShares.forEach {  share ->
                share.called = false
            }
            pref.saveWatchedShares(listOfWatchedShares)
        }
        currentCryptoBuyAt = pref.getBuyAt(currencyToCall?.name?:"")
            .asLiveData()
            .value
            ?.ifEmpty { "0" }!!
            .toDouble()
        currentCryptoSellAt = pref.getSellAt(currencyToCall?.name?:"")
            .asLiveData()
            .value
            ?.ifEmpty { "0" }!!
            .toDouble()

        repo.getCrypto(currencyToCall?.id?:"")
            .catch { e ->  }
            .collect { cryptoList ->
                cryptoList[0].let { crypto ->
                    Log.d("makeHttpCalls", "name: ${currencyToCall?.name} value: "+ crypto.open.toString())
                    try{
                        listOfWatchedShares.find { it.id == currencyToCall?.id }?.called = true
                        pref.saveWatchedShares(listOfWatchedShares)
                        val allAreProcessed = listOfWatchedShares.none { !it.called }
                        if(allAreProcessed) {
                            listOfWatchedShares.forEach {  share ->
                                share.called = false
                            }
                            pref.saveWatchedShares(listOfWatchedShares)
                        }

                        val amount = if(crypto.open.toString().length > 10) crypto.open.toString().substring(0, 10)
                        else crypto.open
                        notificationContent = "Current: $amount" +
                                "\nBuy at : $currentCryptoBuyAt" +
                                "\nSell at : $currentCryptoSellAt"

                        if(shouldDisplayNotification(
                                amount = amount.toString().toDouble(),
                                low = currentCryptoBuyAt,
                                high = currentCryptoSellAt)) {
                            NotificationsUtils
                                .showCryptoNotifications(
                                    notificationContent,
                                    currencyToCall?.name?:"",
                                    context)

                        }
                        shouldRetry(false)

                    } catch (e: Exception) {
                        shouldRetry(true)
                        NotificationsUtils
                            .showWorkerNotifications(
                                "Exception occurred: "+e.message.toString(),
                                context)
                    }
                }
            }

        Log.d("makeHttpCalls", "Job started")
    }

    private fun shouldDisplayNotification(amount: Double, high: Double, low: Double) : Boolean {
        return  amount.toString().toDouble() <= low || amount.toString().toDouble() >= high
    }
}

actual object BackgroundSync {

    /**
     * Call this once from your Android Application.onCreate:
     *
     *   AppCtx.init(this)
     */
//    fun ensureInitCalled() {
//        check(::AppCtx.isInitialized.not()) { "No-op: already initialized" }
//    }

    actual fun schedule(baseUrl: String, path: String, intervalMinutes: Int) {
        val ctx = AppCtx.context
        val data = Data.Builder()
            .putString("BASE_URL", baseUrl)
            .putString("PATH", path)
            .build()

        val request = PeriodicWorkRequestBuilder<SyncWorker>(
            intervalMinutes.toLong().coerceAtLeast(15L), // WorkManager min = 15min
            TimeUnit.MINUTES
        )
            .setInputData(data)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
            "background.sync.job",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    actual suspend fun runOnceNow(baseUrl: String, path: String) {
        val ctx = AppCtx.context
        val data = Data.Builder()
            .putString("BASE_URL", baseUrl)
            .putString("PATH", path)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(data)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(ctx)
            .enqueue(request)
    }
}
