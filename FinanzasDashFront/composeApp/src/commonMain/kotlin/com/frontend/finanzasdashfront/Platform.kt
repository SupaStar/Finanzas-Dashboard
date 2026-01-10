package com.frontend.finanzasdashfront

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform