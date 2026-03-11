package com.frontend.finanzasdashfront.routes.flows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.routes.DashboardScreenRoute
import com.frontend.finanzasdashfront.routes.routers.DashboardRouter
import com.frontend.finanzasdashfront.routes.routers.DashboardScreens
import com.frontend.finanzasdashfront.views.portfolio.PortfolioItemScreen
import com.frontend.finanzasdashfront.views.portfolio.PortfolioFixedItemScreen

@Composable
fun DashboardNavigationFlow() {
    val router = AppModule.dashboardRouter
    val currentScreen = router.current
    when (val screen = currentScreen) {
        is DashboardScreens.Dashboard -> {
            val viewModel = remember { AppModule.provideDashboardViewModel() }
            val viewModelSelectStock = remember { AppModule.provideSelectStockVM() }
            val addFixedPortfolioModalVM = remember { AppModule.provideAddFixedPortfolioModalVM() }
            DashboardScreenRoute(viewModel, viewModelSelectStock, addFixedPortfolioModalVM)
        }

        is DashboardScreens.PortfolioDetail -> {
            val viewModel = remember { AppModule.providePortfolioViewModel(screen.idPortfolio) }
            val addOperationModalVM = remember { AppModule.provideAddOperationVM() }
            val addDividendModalVM = remember { AppModule.provideAddDividendVM() }
            PortfolioItemScreen(
                onBack = { router.goTo(DashboardScreens.Dashboard) },
                viewModel,
                addOperationVM = addOperationModalVM,
                addDividendVm = addDividendModalVM
            )
        }

        is DashboardScreens.FixedPortfolioDetail -> {
            val viewModel = remember { AppModule.providePortfolioFixedItemVM(screen.idPortfolio) }
            PortfolioFixedItemScreen(
                onBack = { router.goTo(DashboardScreens.Dashboard) },
                viewModel = viewModel
            )
        }
    }
}