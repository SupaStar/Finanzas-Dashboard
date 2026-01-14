package com.frontend.finanzasdashfront.routes

import androidx.compose.runtime.Composable
import com.frontend.finanzasdashfront.views.dashboard.DashboardScreen
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel
import com.frontend.finanzasdashfront.viewmodel.dashboard.stock.SelectStockVM

@Composable
fun DashboardScreenRoute(viewModel: DashboardViewModel, selectStockVM: SelectStockVM) {
    DashboardScreen(
        viewModel = viewModel,
        viewModelModal = selectStockVM
    )
}