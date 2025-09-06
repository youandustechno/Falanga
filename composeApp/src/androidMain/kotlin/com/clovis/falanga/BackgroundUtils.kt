package com.clovis.falanga

// BackgroundSyncWorker.kt
import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.clovis.falanga.models.CryptoApiClient
import com.clovis.falanga.models.CryptoRepository
import com.clovis.falanga.models.platformHttpClient
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import java.util.concurrent.TimeUnit

class BackgroundSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private var thisContext = context

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


class BackgroundSyncManager(private val context: Context) {

    fun startPeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncWork = PeriodicWorkRequestBuilder<BackgroundSyncWorker>(
            30, TimeUnit.MINUTES, // repeatInterval
            5, TimeUnit.MINUTES   // flexInterval
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "background_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                syncWork
            )
    }

    fun stopPeriodicSync() {
        WorkManager.getInstance(context)
            .cancelUniqueWork("background_sync")
    }
}