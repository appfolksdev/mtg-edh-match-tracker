package com.appfolks.mtgedhmatchtracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform