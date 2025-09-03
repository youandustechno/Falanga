package com.clovis.falanga

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

object StringsUtil {
    var BASE_URL = ""
    var DOLLARS = "$"
    var SEPARATOR = " ^ "
    var SELL_AT = "SellAt"
    var BUY_AT = "BuyAt"
    var AVERAGE = "Average"
    var QUANTITY = "Quantity"
    var PRECISION = "Precision"
    var EMPTY = ""
    var WORK_NAME = "PeriodicHttpRequest"
    var WATCHED = "Watched"

    fun convertAmountToDouble(value: String ): String {
        val newValue = if(value.endsWith(".0")){
            value+"0"
        } else if(value.endsWith(".00")) {
            value
        } else if(value.contains(".")){
            value
        }
        else {
            value+".00"
        }
        val cleanedInput =  try{
            newValue.replace("[^\\d]".toRegex(), EMPTY)

        } catch (e: Exception) {
            newValue
        }
        val parsedValue = cleanedInput.toLong()
        val dollarValue = parsedValue/100.0

        return dollarValue.toString()
    }

    fun convertAmountWithPrecision(value: String, precision: String = "0"): String {

        return  if(precision.isNotEmpty()) {
            val cleanedInput =  try{
                value.replace("[^\\d]".toRegex(), EMPTY)

            } catch (e: Exception) {
                value
            }
            val parsedValue = cleanedInput.ifEmpty { "0" }.toLong()
            var quotient = ""+ 1
            for(i in 1 .. precision.toInt()) {
                quotient += "0"
            }
            val dollarValue: Double = parsedValue.toDouble()/quotient.toDouble()

            dollarValue.toString()
        } else {
            value
        }
    }
}
