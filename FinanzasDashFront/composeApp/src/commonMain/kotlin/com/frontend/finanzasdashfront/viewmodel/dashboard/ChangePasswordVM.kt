package com.frontend.finanzasdashfront.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
)

class ChangePasswordVM(private val authService: AuthService) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onCurrentPasswordChanged(value: String) =
        _uiState.update { it.copy(currentPassword = value, errorMessage = "", successMessage = "") }

    fun onNewPasswordChanged(value: String) =
        _uiState.update { it.copy(newPassword = value, errorMessage = "", successMessage = "") }

    fun onConfirmPasswordChanged(value: String) =
        _uiState.update { it.copy(confirmPassword = value, errorMessage = "", successMessage = "") }

    fun toggleCurrentPasswordVisible() =
        _uiState.update { it.copy(isCurrentPasswordVisible = !it.isCurrentPasswordVisible) }

    fun toggleNewPasswordVisible() =
        _uiState.update { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }

    fun toggleConfirmPasswordVisible() =
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }

    fun changePassword() {
        val state = _uiState.value

        // Client-side validation
        if (state.currentPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa tu contraseña actual") }
            return
        }
        if (state.newPassword.length < 6) {
            _uiState.update { it.copy(errorMessage = "La nueva contraseña debe tener al menos 6 caracteres") }
            return
        }
        if (state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las nuevas contraseñas no coinciden") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }
        viewModelScope.launch {
            try {
                authService.changePassword(state.currentPassword, state.newPassword, state.confirmPassword)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Contraseña actualizada correctamente",
                        currentPassword = "",
                        newPassword = "",
                        confirmPassword = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cambiar la contraseña"
                    )
                }
            }
        }
    }

    fun reset() = _uiState.update { ChangePasswordUiState() }
}
