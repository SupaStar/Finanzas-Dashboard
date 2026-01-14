package com.frontend.finanzasdashfront.routes.flows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.routes.DashboardScreenRoute
import com.frontend.finanzasdashfront.routes.routers.DashboardRouter
import com.frontend.finanzasdashfront.routes.routers.DashboardScreens
import com.frontend.finanzasdashfront.views.portfolio.PortfolioItemScreen

@Composable
fun DashboardNavigationFlow() {
    val router = AppModule.dashboardRouter
    val currentScreen = router.current
    when (val screen = currentScreen) {
        is DashboardScreens.Dashboard -> {
            val viewModel = remember { AppModule.provideDashboardViewModel() }
            val viewModelSelectStock = remember { AppModule.provideSelectStockVM() }
            DashboardScreenRoute(viewModel, viewModelSelectStock)
        }

        is DashboardScreens.PortfolioDetail -> {
            val viewModel = remember { AppModule.providePortfolioViewModel(screen.idPortfolio) }
            PortfolioItemScreen(
                onBack = { router.goTo(DashboardScreens.Dashboard) },
                viewModel
            )
        }
    }
}