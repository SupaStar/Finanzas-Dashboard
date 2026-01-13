package com.frontend.finanzasdashfront.routes.routers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class AuthScreen {
    object Login : AuthScreen()
}

class AuthRouter {
    var current by mutableStateOf<AuthScreen>(AuthScreen.Login)
        private set

    fun goTo(screen: AuthScreen) {
        current = screen
    }
}