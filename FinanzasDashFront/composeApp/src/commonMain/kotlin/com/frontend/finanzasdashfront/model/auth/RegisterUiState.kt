package com.frontend.finanzasdashfront.model.auth

data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val passwordConfirmation: String = "",
    val isPasswordConfirmationVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)