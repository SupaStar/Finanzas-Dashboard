package com.frontend.finanzasdashfront.routes.routers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class AuthScreens {
    object Login : AuthScreens()
    object Register: AuthScreens()
}

class AuthRouter {
    var current by mutableStateOf<AuthScreens>(AuthScreens.Login)
        private set

    fun goTo(screen: AuthScreens) {
        current = screen
    }
}