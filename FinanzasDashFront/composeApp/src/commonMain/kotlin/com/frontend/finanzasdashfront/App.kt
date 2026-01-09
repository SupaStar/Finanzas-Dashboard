package com.frontend.finanzasdashfront

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.frontend.finanzasdashfront.routes.LoginScreenRoute
import com.frontend.finanzasdashfront.viewmodel.auth.LoginViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import finanzasdashfront.composeapp.generated.resources.Res
import finanzasdashfront.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    var startDestination by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
//        val token = tokenManager.getToken()
        val token = null
        startDestination = if (token != null) "dashboard" else "login"
    }
    MaterialTheme {
        when (startDestination) {
            "login" -> {
                val viewModel = remember { AppModule.provideLoginViewModel() }
                LoginScreenRoute(viewModel)
            }
            null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }


}