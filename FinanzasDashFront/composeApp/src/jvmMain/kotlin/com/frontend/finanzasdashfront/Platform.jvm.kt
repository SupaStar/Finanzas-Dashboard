package com.frontend.finanzasdashfront

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
//Local
actual fun getPlatformHost(): String = "http://localhost:8080"
// Server local
//actual fun getPlatformHost(): String = "http://192.168.1.96:8080"
