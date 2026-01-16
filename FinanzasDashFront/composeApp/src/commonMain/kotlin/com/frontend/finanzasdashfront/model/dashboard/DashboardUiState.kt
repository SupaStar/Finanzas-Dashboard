package com.frontend.finanzasdashfront.model.dashboard

import androidx.compose.ui.graphics.Color
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioDto

data class DashboardUiState (
    val isLoading: Boolean = false,
    val items: List<PortfolioDto> = emptyList(),
    val errorMessage: String? = null,
    val totalValue: Double = 0.0,
    val chartData: DataPieChartDashboard = DataPieChartDashboard()
)

data class DataPieChartDashboard(
    val data: List<DataPieChart> = emptyList(),
    val colors: List<Color> = emptyList(),
)

data class DataPieChart(
    val value: Float = 0.0f,
    val label: String = "",
)