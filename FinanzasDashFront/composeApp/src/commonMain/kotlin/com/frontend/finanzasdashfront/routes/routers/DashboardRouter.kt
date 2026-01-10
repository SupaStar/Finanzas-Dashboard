package com.frontend.finanzasdashfront.routes.routers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class DashboardScreens {
    object Dashboard : DashboardScreens()
}
class DashboardRouter {
    var current by mutableStateOf<DashboardScreens>(DashboardScreens.Dashboard)
        private set

    fun goTo(screen: DashboardScreens) {
        current = screen
    }
}