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
//Local Sercer
//actual fun getPlatformHost(): String = "http://192.168.1.96:8082/api"
//PROD AWS
//actual fun getPlatformHost(): String = "http://3.144.13.186:8082/api"
//PROD oracle
actual fun getPlatformHost(): String = "http://139.177.102.0:8082/api"
//actual fun getPlatformHost(): String = "http://localhost:8080"
