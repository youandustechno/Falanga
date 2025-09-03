package com.clovis.falanga

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform