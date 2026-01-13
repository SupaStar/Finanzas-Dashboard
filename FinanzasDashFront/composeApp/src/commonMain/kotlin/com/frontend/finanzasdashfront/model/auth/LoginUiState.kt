package com.frontend.finanzasdashfront.model.auth

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)