package com.frontend.finanzasdashfront

import kotlinx.browser.window

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun isDebugBuild(): Boolean {
    // En Wasm, puedes verificar si estamos en modo desarrollo
    // Por ahora, retornamos true para desarrollo
    return true // TODO: Implementar detección real de modo debug en Wasm
}

// Dynamically points to the current origin where Nginx hosts the API
actual fun getPlatformHost(): String = window.location.origin + "/api"
