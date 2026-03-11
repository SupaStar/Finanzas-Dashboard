package com.frontend.finanzasdashfront.routes

import androidx.compose.runtime.Composable
import com.frontend.finanzasdashfront.views.dashboard.DashboardScreen
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel
import com.frontend.finanzasdashfront.viewmodel.dashboard.stock.SelectStockVM
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddFixedPortfolioModalVM

@Composable
fun DashboardScreenRoute(
    viewModel: DashboardViewModel, 
    selectStockVM: SelectStockVM,
    addFixedPortfolioModalVM: AddFixedPortfolioModalVM
) {
    DashboardScreen(
        viewModel = viewModel,
        viewModelModal = selectStockVM,
        addFixedPortfolioModalVM = addFixedPortfolioModalVM
    )
}