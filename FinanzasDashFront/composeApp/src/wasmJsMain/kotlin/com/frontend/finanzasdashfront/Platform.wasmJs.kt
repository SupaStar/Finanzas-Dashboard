package com.frontend.finanzasdashfront

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun isDebugBuild(): Boolean {
    // En Wasm, puedes verificar si estamos en modo desarrollo
    // Por ahora, retornamos true para desarrollo
    return true // TODO: Implementar detección real de modo debug en Wasm
}

actual fun getPlatformHost(): String = "http://localhost:8082/api"