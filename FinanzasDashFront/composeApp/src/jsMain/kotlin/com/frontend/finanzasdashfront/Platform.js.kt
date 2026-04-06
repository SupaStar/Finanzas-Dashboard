package com.frontend.finanzasdashfront

import kotlinx.browser.window

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()

actual fun isDebugBuild(): Boolean {
    // En JS, puedes verificar si estamos en modo desarrollo
    // Por ahora, retornamos true para desarrollo
    return true // TODO: Implementar detección real de modo debug en JS
}

actual fun getPlatformHost(): String = window.location.origin + "/api"