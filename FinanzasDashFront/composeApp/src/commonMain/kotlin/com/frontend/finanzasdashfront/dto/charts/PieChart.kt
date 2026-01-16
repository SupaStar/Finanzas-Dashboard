package com.frontend.finanzasdashfront.dto.charts

import androidx.compose.ui.graphics.Color

data class DataPieChartDashboard(
    val data: List<DataPieChart> = emptyList(),
    val colors: List<Color> = emptyList(),
)

data class DataPieChart(
    val value: Float = 0.0f,
    val label: String = "",
)