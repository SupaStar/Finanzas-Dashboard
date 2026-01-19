package com.frontend.finanzasdashfront.routes.flows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.routes.LoginScreenRoute
import com.frontend.finanzasdashfront.routes.routers.AuthRouter
import com.frontend.finanzasdashfront.routes.routers.AuthScreens
import com.frontend.finanzasdashfront.views.auth.RegisterScreen

@Composable
fun AuthNavigationFlow() {
    val router = AppModule.authRouter
    val currentScreen = router.current

    when (val screen = currentScreen) {
        is AuthScreens.Login -> {
            val viewModel = remember { AppModule.provideLoginViewModel() }
            LoginScreenRoute(viewModel)
        }

        is AuthScreens.Register -> {
            val viewModel = remember { AppModule.provideRegisterViewModel() }
            RegisterScreen(onBack = { router.goTo(AuthScreens.Login) }, viewModel)
        }
    }
}