package com.clovis.falanga

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.clovis.falanga.StringsUtil.AVERAGE
import com.clovis.falanga.StringsUtil.BUY_AT
import com.clovis.falanga.StringsUtil.EMPTY
import com.clovis.falanga.StringsUtil.PRECISION
import com.clovis.falanga.StringsUtil.QUANTITY
import com.clovis.falanga.StringsUtil.SELL_AT
import com.clovis.falanga.StringsUtil.WATCHED

class PreferenceSUtil(private var context: Context) {

    private val sharedPreferences by lazy { context.getSharedPreferences("MySharedPref",
        MODE_PRIVATE
    )}

    private val gson = Gson()

    private var edit: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveSellAt(value: String,cryptoName: String) {
        edit.putString(SELL_AT+cryptoName, value).apply()
    }

    fun getSellAt(cryptoName: String): String {
        return sharedPreferences.getString(SELL_AT+cryptoName, EMPTY)?: EMPTY
    }

    fun saveBuyAt(value: String, cryptoName: String) {
        edit.putString(BUY_AT+cryptoName, value).apply()

    }

    fun getBuyAt(cryptoName: String): String {
        return sharedPreferences.getString(BUY_AT+cryptoName, EMPTY)?: EMPTY
    }

    fun saveAverage(value: String,cryptoName: String) {
        edit.putString(AVERAGE+cryptoName, value).apply()
    }

    fun getAverage(cryptoName: String): String {
        return sharedPreferences.getString(AVERAGE+cryptoName, EMPTY)?: EMPTY
    }

    fun saveQuantity(value: String, cryptoName: String) {
        edit.putString(QUANTITY+cryptoName, value).apply()
    }

    fun getQuantity(cryptoName: String): String {
        return sharedPreferences.getString(QUANTITY+cryptoName, EMPTY)?: EMPTY
    }

    fun savePrecision(value: String, cryptoName: String, precisionName: String) {
        edit.putString(PRECISION+cryptoName+precisionName, value).apply()
    }

    fun getPrecision(cryptoName: String, precisionName: String): String {
        return sharedPreferences.getString(PRECISION+cryptoName+precisionName, EMPTY)?: EMPTY
    }

    fun saveWatchedShares(list: List<WatchedShares>) {
        val json = gson.toJson(list)
        edit.putString(WATCHED, json).apply()
    }

    fun getWatchedShares() : List<WatchedShares> {
        val json = sharedPreferences.getString(WATCHED, null) ?: return emptyList()
        val type = object: TypeToken<List<WatchedShares>> () {}.type

        return gson.fromJson(json, type)
    }
}

data class WatchedShares(
    var id: String,
    var name: String,
    var called: Boolean = false
)

