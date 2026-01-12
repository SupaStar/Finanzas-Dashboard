package com.frontend.finanzasdashfront.model.dashboard

import com.frontend.finanzasdashfront.dto.portfolio.PortfolioDto

data class DashboardUiState (
    val items:List<PortfolioDto> = emptyList()
)