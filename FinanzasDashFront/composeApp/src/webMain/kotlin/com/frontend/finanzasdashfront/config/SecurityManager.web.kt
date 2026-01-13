package com.frontend.finanzasdashfront.config

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual class SecurityManager {
    actual fun createSettings(): Settings {
        return Settings()
    }
}