package com.frontend.finanzasdashfront

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual fun getPlatformHost(): String = "http://localhost:8080"