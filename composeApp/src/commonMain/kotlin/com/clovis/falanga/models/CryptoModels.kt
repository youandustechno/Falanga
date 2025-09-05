package com.clovis.falanga.models

import kotlinx.serialization.Serializable

@Serializable
data class Crypto(
    var id : String, //"btc-bitcoin"
    var name :String,  //"Bitcoin",
    var symbol: String, //"BTC",
    var rank: Long, //1,
    var is_new: Boolean, //":false,
    var is_active: Boolean, //true,
    var type: String //"coin"
)

@Serializable
data class CryptoUpdate(
    var time_open: String, //"2018-03-01T00:00:00Z",
    var time_close: String, // "2018-03-01T23:59:59Z",
    var open: Double, //856.012,
    var high: Double, //880.302,
    var low: Double, //851.92,
    var close: Double, //872.2,
    var volume: Long, //1868520000,
    var market_cap: Long //83808161204
)