package com.clovis.falanga.models

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual fun platformHttpClient(): HttpClient =
    HttpClient(Js) {
        install(ContentNegotiation) { json(KmpJson) }

        // ⚠️ HttpTimeout is not fully supported in browsers,
        // but you can still configure it. Some values may be ignored.
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
    }