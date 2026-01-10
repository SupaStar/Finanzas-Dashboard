package com.frontend.finanzasdashfront.config

import com.russhwolf.settings.Settings

expect class SecurityManager() {
    fun createSettings():Settings
}