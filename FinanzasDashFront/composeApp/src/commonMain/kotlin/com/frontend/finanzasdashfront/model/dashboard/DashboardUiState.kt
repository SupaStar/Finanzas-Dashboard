package com.frontend.finanzasdashfront.model.dashboard

import com.frontend.finanzasdashfront.dto.portfolio.PortfolioDto

data class DashboardUiState (
    val isLoading: Boolean = false,
    val items:List<PortfolioDto> = emptyList()
)