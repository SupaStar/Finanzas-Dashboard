package com.frontend.finanzasdashfront.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.auth.AuthService
import com.frontend.finanzasdashfront.model.auth.LoginUiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class LoginViewModel(private val authService: AuthService): ViewModel() {

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

                val owo = ""
//                // 3. Si tiene éxito, guardamos el token
//                tokenManager.saveToken(response.token)
//
//                // 4. Actualizamos el estado para navegar o mostrar éxito
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    isSuccess = true // Asumiendo que tienes este campo en tu LoginUiState
//                )

            } catch (e: Exception) {
                // 5. Manejo de errores (Network, 401 Unauthorized, etc.)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error inesperado al conectar con el servidor"
                )
            }
        }

    }
}


