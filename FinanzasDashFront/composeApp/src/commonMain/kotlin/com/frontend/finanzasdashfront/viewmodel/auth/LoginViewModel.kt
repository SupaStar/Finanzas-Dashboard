package com.frontend.finanzasdashfront.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.AuthService
import com.frontend.finanzasdashfront.config.TokenManager
import com.frontend.finanzasdashfront.model.auth.LoginUiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class LoginViewModel(private val authService: AuthService, private val tokenManager: TokenManager): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Usuario y contraseña son obligatorios"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        viewModelScope.launch {
            try {
                val response = authService.login(username, password)

                if (response.estado && response.message?.token != null) {
                    tokenManager.saveToken(response.message.token)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = response.errors?.joinToString("\n") ?: "Error al iniciar sesión"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error inesperado al conectar con el servidor"
                )
            }
        }

    }
}


