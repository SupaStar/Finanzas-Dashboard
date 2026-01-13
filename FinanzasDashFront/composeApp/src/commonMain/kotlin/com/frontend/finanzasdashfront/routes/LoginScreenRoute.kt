package com.frontend.finanzasdashfront.routes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.frontend.finanzasdashfront.views.auth.LoginScreen
import com.frontend.finanzasdashfront.viewmodel.auth.LoginViewModel

@Composable
fun LoginScreenRoute(viewModel: LoginViewModel) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoggedIn) {
        Text("✅ Sesión iniciada")
    } else {
        LoginScreen(
            onLogin = viewModel::login,
            errorMessage = state.errorMessage,
            isLoading = state.isLoading
        )
    }
}