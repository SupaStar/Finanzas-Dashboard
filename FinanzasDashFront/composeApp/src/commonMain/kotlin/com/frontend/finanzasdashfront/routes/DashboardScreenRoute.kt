package com.frontend.finanzasdashfront.routes

import androidx.compose.runtime.Composable
import com.frontend.finanzasdashfront.views.dashboard.DashboardScreen
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel

@Composable
fun DashboardScreenRoute(viewModel: DashboardViewModel) {
    DashboardScreen(onLogout = viewModel::logout, viewModel = viewModel, onNavigateToPortfolioDetail = viewModel::goToDetail)
}