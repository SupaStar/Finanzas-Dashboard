package com.frontend.finanzasdashfront

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun getPlatformHost(): String
expect fun getCurrentDateParams(): Pair<Int, Int>