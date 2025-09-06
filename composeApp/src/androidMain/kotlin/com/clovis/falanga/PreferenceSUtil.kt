package com.clovis.falanga

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.clovis.falanga.StringsUtil.AVERAGE
import com.clovis.falanga.StringsUtil.BUY_AT
import com.clovis.falanga.StringsUtil.EMPTY
import com.clovis.falanga.StringsUtil.PRECISION
import com.clovis.falanga.StringsUtil.QUANTITY
import com.clovis.falanga.StringsUtil.SELL_AT
import com.clovis.falanga.StringsUtil.WATCHED
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PreferenceAndroid(private var context: Context): PreferenceS {

    private val sharedPreferences by lazy { context.getSharedPreferences("MySharedPref",
        MODE_PRIVATE
    )}

    private val gson = Gson()

    private var edit: SharedPreferences.Editor = sharedPreferences.edit()

    override suspend fun saveSellAt(value: String,cryptoName: String) {
        edit.putString(SELL_AT+cryptoName, value).apply()
    }

    override  fun getSellAt(cryptoName: String): Flow<String> {
        return flow {sharedPreferences.getString(SELL_AT+cryptoName, EMPTY)?: EMPTY}
    }

    override suspend fun saveBuyAt(value: String, cryptoName: String) {
        edit.putString(BUY_AT+cryptoName, value).apply()

    }

    override  fun getBuyAt(cryptoName: String):  Flow<String>  {
        return flow { sharedPreferences.getString(BUY_AT+cryptoName, EMPTY)?: EMPTY  }
    }

    override suspend fun saveAverage(value: String,cryptoName: String) {
        edit.putString(AVERAGE+cryptoName, value).apply()
    }

    override  fun getAverage(cryptoName: String): Flow<String>  {
        return flow { sharedPreferences.getString(AVERAGE+cryptoName, EMPTY)?: EMPTY }
    }

    override suspend fun saveQuantity(value: String, cryptoName: String) {
        edit.putString(QUANTITY+cryptoName, value).apply()
    }

    override fun getQuantity(cryptoName: String): Flow<String>  {
        return flow { sharedPreferences.getString(QUANTITY+cryptoName, EMPTY)?: EMPTY }
    }

    override suspend fun savePrecision(value: String, cryptoName: String, precisionName: String) {
        edit.putString(PRECISION+cryptoName+precisionName, value).apply()
    }

    override fun getPrecision(cryptoName: String, precisionName: String): Flow<String> {
        return flow {sharedPreferences.getString(PRECISION+cryptoName+precisionName, EMPTY)?: EMPTY }
    }

    override suspend fun saveWatchedShares(list: List<WatchedShares>) {
        val json = gson.toJson(list)
        edit.putString(WATCHED, json).apply()
    }

    override suspend fun getWatchedShares() : List<WatchedShares> {
        val json = sharedPreferences.getString(WATCHED, null) ?: return emptyList()
        val type = object: TypeToken<List<WatchedShares>> () {}.type

        return gson.fromJson(json, type)
    }
}