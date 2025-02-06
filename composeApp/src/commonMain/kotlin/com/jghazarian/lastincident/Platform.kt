package com.jghazarian.lastincident

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform