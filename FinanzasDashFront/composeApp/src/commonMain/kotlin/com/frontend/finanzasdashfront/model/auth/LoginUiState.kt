package com.frontend.finanzasdashfront.model.auth

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val username: String = "",
    val password: String = "",
)