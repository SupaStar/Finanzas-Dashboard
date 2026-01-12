package com.frontend.finanzasdashfront.routes.routers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class DashboardScreens {
    object Dashboard : DashboardScreens()
    data class PortfolioDetail(val idPortfolio: Long) : DashboardScreens()
}

class DashboardRouter {
    var current by mutableStateOf<DashboardScreens>(DashboardScreens.Dashboard)
        private set

    fun goTo(screen: DashboardScreens) {
        current = screen
    }
}