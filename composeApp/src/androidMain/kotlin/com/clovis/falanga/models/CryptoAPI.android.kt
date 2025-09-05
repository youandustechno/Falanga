package com.clovis.falanga.models

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual fun platformHttpClient(): HttpClient =
    HttpClient(OkHttp) {
        install(ContentNegotiation) { json(KmpJson) }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
        // You can add OkHttp interceptors here if needed via OkHttpConfig
    }