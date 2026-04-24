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

actual fun getCurrentDateParams(): Pair<Int, Int> {
    val date = platform.Foundation.NSDate()
    val calendar = platform.Foundation.NSCalendar.currentCalendar
    val components = calendar.components(
        platform.Foundation.NSCalendarUnitMonth or platform.Foundation.NSCalendarUnitYear,
        fromDate = date
    )
    return Pair(components.month.toInt(), components.year.toInt())
}