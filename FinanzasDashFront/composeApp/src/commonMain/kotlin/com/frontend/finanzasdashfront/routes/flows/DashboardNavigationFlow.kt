package com.frontend.finanzasdashfront.routes.flows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.routes.DashboardScreenRoute
import com.frontend.finanzasdashfront.routes.routers.DashboardRouter
import com.frontend.finanzasdashfront.routes.routers.DashboardScreens

@Composable
fun DashboardNavigationFlow() {
    val router = remember { DashboardRouter() }

    when (router.current) {
        DashboardScreens.Dashboard -> {
            val viewModel = remember { AppModule.provideDashboardViewModel() }
            DashboardScreenRoute(viewModel)
        }
    }
}