package com.frontend.finanzasdashfront

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.frontend.finanzasdashfront.routes.flows.AuthNavigationFlow
import com.frontend.finanzasdashfront.routes.flows.DashboardNavigationFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val tokenManager = remember { AppModule.tokenManager }
    val currentToken by tokenManager.token.collectAsState()
    var startDestination by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        val token = tokenManager.getToken()
        startDestination = if (token != null) "dashboard" else "login"
    }
    MaterialTheme {
        if (currentToken == null) {
            AuthNavigationFlow()
        } else {
            DashboardNavigationFlow()
        }
    }
}

