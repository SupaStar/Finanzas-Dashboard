package com.frontend.finanzasdashfront.config

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TokenManager(private val securityManager: SecurityManager) {
    private val settings: Settings = securityManager.createSettings()
    private val _token = MutableStateFlow(settings.getStringOrNull("jwt_token"))
    val token: StateFlow<String?> = _token.asStateFlow()
    fun saveToken(token: String) {
        settings.putString("jwt_token", token)
        _token.value = token
    }

    fun getToken(): String? {
        return settings.getStringOrNull("jwt_token")
    }

    fun clearToken() {
        settings.remove("jwt_token")
        _token.value = null
    }
}