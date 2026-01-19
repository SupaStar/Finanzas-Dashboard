package com.frontend.finanzasdashfront.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.AuthService
import com.frontend.finanzasdashfront.config.TokenManager
import com.frontend.finanzasdashfront.dto.enums.ProviderEnum
import com.frontend.finanzasdashfront.dto.request.RegisterRequestDto
import com.frontend.finanzasdashfront.getPlatform
import com.frontend.finanzasdashfront.model.auth.RegisterUiState
import com.frontend.finanzasdashfront.routes.routers.AuthRouter
import com.frontend.finanzasdashfront.routes.routers.AuthScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val authRouter: AuthRouter
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState


    fun register() {
        if (_uiState.value.username.isEmpty() || _uiState.value.password.isEmpty() || _uiState.value.passwordConfirmation.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Llena todos los campos") }
            return
        }
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = ""
        )
        viewModelScope.launch {
            try {
                val request = RegisterRequestDto(
                    username = _uiState.value.username,
                    password = _uiState.value.password,
                    password_confirmation = _uiState.value.passwordConfirmation,
                    provider = ProviderEnum.local,
                    platform_name = getPlatform().name
                )

                val response = authService.register(request)

                if (response.estado && response.message?.token != null) {
                    authRouter.goTo(AuthScreens.Login)
                    tokenManager.saveToken(response.message.token)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = ""
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

    fun onUsernameChanged(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onIsPasswordVisibleChanged(isPasswordVisible: Boolean) {
        _uiState.update { it.copy(isPasswordVisible = isPasswordVisible) }
    }

    fun onPasswordConfirmChanged(confirmPassword: String) {
        _uiState.update { it.copy(passwordConfirmation = confirmPassword) }
    }

    fun onIsPasswordConfirmVisibleChanged(isPasswordConfirmationVisible: Boolean) {
        _uiState.update { it.copy(isPasswordConfirmationVisible = isPasswordConfirmationVisible) }
    }
}