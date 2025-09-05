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

class PreferenceSUtil(private var prefs: DataStore<Preferences>) {

    suspend fun saveSellAt(value: String, cryptoName: String) {
        val key = stringPreferencesKey(SELL_AT+cryptoName)
        prefs.edit { dataStore ->
            dataStore[key] = value
        }
    }

    fun getSellAt(cryptoName: String): Flow<String> {
        val key = stringPreferencesKey(SELL_AT+cryptoName)
        return prefs.data.map {
            it[key]?: EMPTY
        }
    }

    suspend fun saveBuyAt(value: String, cryptoName: String) {
        val key = stringPreferencesKey(BUY_AT+cryptoName)
        prefs.edit { dataStore ->
            dataStore[key] = value
        }
    }


    fun getBuyAt(cryptoName: String): Flow<String> {
        val key = stringPreferencesKey(BUY_AT+cryptoName)
        return prefs.data.map {
            it[key]?: EMPTY
        }
    }

    suspend fun saveAverage(value: String,cryptoName: String) {
        val key = stringPreferencesKey(AVERAGE+cryptoName)
        prefs.edit { dataStore ->
            dataStore[key] = value
        }
    }

    fun getAverage(cryptoName: String): Flow<String> {
        val key = stringPreferencesKey(AVERAGE+cryptoName)
        return prefs.data.map {
            it[key]?: EMPTY
        }
    }

    suspend fun saveQuantity(value: String,cryptoName: String) {
        val key = stringPreferencesKey(QUANTITY+cryptoName)
        prefs.edit { dataStore ->
            dataStore[key] = value
        }
    }

    fun getQuantity(cryptoName: String): Flow<String> {
        val key = stringPreferencesKey(QUANTITY+cryptoName)
        return prefs.data.map {

            it[key]?: EMPTY
        }
    }


    suspend fun savePrecision(value: String, cryptoName: String, precisionName: String) {
        val key = stringPreferencesKey(PRECISION+cryptoName+precisionName)
        prefs.edit { dataStore ->
            dataStore[key] = value
        }
    }

    fun getPrecision(cryptoName: String, precisionName: String): Flow<String> {
        val key = stringPreferencesKey(PRECISION+cryptoName+precisionName)
        return prefs.data.map {
            it[key]?: EMPTY
        }
    }

    suspend fun saveWatchedShares(list: List<WatchedShares>) {
        val json = Json.encodeToString(list)
        val key = stringPreferencesKey(WATCHED)
        prefs.edit { dataStore ->
            dataStore[key] = json
        }
    }

    suspend fun getWatchedShares() : List<WatchedShares> {
        val key = stringPreferencesKey(WATCHED)
        val jsonString = prefs.data.map { it[key] }.firstOrNull()

        return if(!jsonString.isNullOrEmpty()) {
            Json.decodeFromString(jsonString)
        } else {
            emptyList<WatchedShares>()
        }
    }

//    fun watchedSharesFlow(
//        dataStore: DataStore<Preferences>
//    ) = dataStore.data.map { prefs ->
//        val json = prefs[WATCHED_KEY]
//        if (!json.isNullOrEmpty()) Json.decodeFromString(json) else emptyList()
//    }
}

data class WatchedShares(
    var id: String,
    var name: String,
    var called: Boolean = false
)

