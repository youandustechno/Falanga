package com.clovis.falanga


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.clovis.falanga.StringsUtil.AVERAGE
import com.clovis.falanga.StringsUtil.BUY_AT
import com.clovis.falanga.StringsUtil.EMPTY
import com.clovis.falanga.StringsUtil.PRECISION
import com.clovis.falanga.StringsUtil.QUANTITY
import com.clovis.falanga.StringsUtil.SELL_AT
import com.clovis.falanga.StringsUtil.WATCHED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

interface PreferenceS {

    suspend fun saveSellAt(value: String, cryptoName: String)
    fun getSellAt(cryptoName: String): Flow<String>

    suspend fun saveBuyAt(value: String, cryptoName: String)

    fun getBuyAt(cryptoName: String): Flow<String>

    suspend fun saveAverage(value: String,cryptoName: String)

    fun getAverage(cryptoName: String): Flow<String>

    suspend fun saveQuantity(value: String,cryptoName: String)

    fun getQuantity(cryptoName: String): Flow<String>

    suspend fun savePrecision(value: String, cryptoName: String, precisionName: String)

    fun getPrecision(cryptoName: String, precisionName: String): Flow<String>

    suspend fun saveWatchedShares(list: List<WatchedShares>)

    suspend fun getWatchedShares() : List<WatchedShares>
}

data class WatchedShares(
    var id: String,
    var name: String,
    var called: Boolean = false
)

