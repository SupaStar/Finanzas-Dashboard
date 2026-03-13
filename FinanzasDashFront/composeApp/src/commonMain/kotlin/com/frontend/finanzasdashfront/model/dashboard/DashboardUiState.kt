package com.frontend.finanzasdashfront.model.dashboard

import androidx.compose.ui.graphics.Color
import com.frontend.finanzasdashfront.dto.charts.DataPieChartDashboard
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioDto
import com.frontend.finanzasdashfront.dto.fixed_portfolio.FixedPortfolioResponseDto

data class DashboardUiState (
    val isLoading: Boolean = false,
    val items: List<PortfolioDto> = emptyList(),
    val fixedPortfolios: List<FixedPortfolioResponseDto> = emptyList(),
    val errorMessage: String? = null,
    val totalValue: Double = 0.0,
    val totalValueFixed: Double = 0.0,
    val usdValue: Float = 1.0f,
    val chartData: DataPieChartDashboard = DataPieChartDashboard(),
    val filterStock: String = "",
    val filteredStocks: List<PortfolioDto> = emptyList(),
    val optionsTabs: List<String> = listOf("Acciones", "Info"),
    val selectedTabIndex: Int = 0,
    val showUsdAsMxn: Boolean = false
)