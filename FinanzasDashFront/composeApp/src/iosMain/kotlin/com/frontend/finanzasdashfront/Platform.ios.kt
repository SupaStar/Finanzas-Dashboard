package com.frontend.finanzasdashfront

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun isDebugBuild(): Boolean {
    // En iOS, puedes usar #if DEBUG o verificar configuración
    // Por ahora, retornamos true para desarrollo
    return true // TODO: Implementar detección real de modo debug en iOS
}

actual fun getPlatformHost(): String = "http://localhost:8080"