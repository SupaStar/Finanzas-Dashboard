package com.frontend.finanzasdashfront.routes.flows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.routes.LoginScreenRoute
import com.frontend.finanzasdashfront.routes.routers.AuthRouter
import com.frontend.finanzasdashfront.routes.routers.AuthScreen

@Composable
fun AuthNavigationFlow() {
    val router = remember { AuthRouter() }

    when (router.current) {
        AuthScreen.Login -> {
            val viewModel = remember { AppModule.provideLoginViewModel() }
            LoginScreenRoute(viewModel)
        }
    }
}