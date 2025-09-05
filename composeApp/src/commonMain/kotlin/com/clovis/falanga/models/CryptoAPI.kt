package com.clovis.falanga.models

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

expect fun platformHttpClient(): HttpClient

class CryptoApiClient(
    private val baseUrl: String = "https://api.coinpaprika.com",
    private val client: HttpClient = platformHttpClient()
) {
    suspend fun getCryptoList(apiKey: String? = null): List<Crypto> {
        val resp = client.get("$baseUrl/v1/coins") {
            if (!apiKey.isNullOrBlank()) header("API_KEY", apiKey)
        }
        if (!resp.status.isSuccess()) {
            // Optional: log resp.bodyAsText() for debugging
            throw IllegalStateException("HTTP ${resp.status}")
        }
        return resp.body()
    }

    suspend fun getCryptoToday(coinId: String): List<CryptoUpdate> {
        val resp = client.get("$baseUrl/v1/coins/$coinId/ohlcv/today/")
        if (!resp.status.isSuccess()) {
            throw IllegalStateException("HTTP ${resp.status}")
        }
        return resp.body()
    }
}

class CryptoRepository(private val api: CryptoApiClient) {

    suspend fun getCryptoList(header: String? = null): Flow<List<Crypto>> =
        flow { emit(api.getCryptoList(header)) }
            .flowOn(Dispatchers.Default)

    suspend fun getCrypto(coinId: String): Flow<List<CryptoUpdate>> =
        flow { emit(api.getCryptoToday(coinId)) }
            .flowOn(Dispatchers.Default)
}

// A reusable JSON config (if you want to share it)
val KmpJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}