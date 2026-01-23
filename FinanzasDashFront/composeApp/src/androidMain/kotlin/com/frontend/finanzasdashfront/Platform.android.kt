package com.frontend.finanzasdashfront

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun isDebugBuild(): Boolean {
    // En Android, BuildConfig.DEBUG está disponible automáticamente
    // Si hay problemas, puedes usar: return true // Para desarrollo
    return try {
        val buildConfigClass = Class.forName("com.frontend.finanzasdashfront.BuildConfig")
        buildConfigClass.getField("DEBUG").getBoolean(null)
    } catch (e: Exception) {
        true // Por defecto true para desarrollo si no se puede detectar
    }
}
//Local
//actual fun getPlatformHost(): String = "http://10.0.2.2:8080"

// Server local
//actual fun getPlatformHost(): String = "http://192.168.1.96:8080"
//PROD aws
actual fun getPlatformHost(): String = "http://3.144.13.186:8082/api"
