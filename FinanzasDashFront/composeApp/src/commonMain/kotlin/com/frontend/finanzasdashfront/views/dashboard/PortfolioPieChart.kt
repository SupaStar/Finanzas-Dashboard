package com.frontend.finanzasdashfront.views.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontend.finanzasdashfront.model.dashboard.DashboardUiState
import com.frontend.finanzasdashfront.utils.formatCurrency
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.legend.FlowLegend
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PortfolioPieChart(
    uiState: DashboardUiState,
    modifier: Modifier = Modifier
) {
    val chartData = uiState.chartData.data
    val values = chartData.map { it.value }
    val colors = uiState.chartData.colors

    ChartLayout(
        modifier = modifier.fillMaxWidth().height(300.dp).padding(16.dp),
        title = {
            Text(
                "Distribución de Portafolio",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        legend = {
            FlowLegend(
                itemCount = chartData.size,
                symbol = { i ->
                    Symbol(
                        modifier = Modifier.size(12.dp),
                        fillBrush = SolidColor(colors.getOrElse(i) { Color.Gray })
                    )
                },
                label = { i ->
                    Column(modifier = Modifier.padding(bottom = 4.dp)) {
                        Text(chartData[i].label, style = MaterialTheme.typography.bodySmall)
                        Text(chartData[i].value.formatCurrency(), style = MaterialTheme.typography.bodySmall, fontSize = 8.sp)
                    }

                },
                modifier = Modifier.padding(top = 16.dp)
            )
        },
        legendLocation = LegendLocation.BOTTOM
    ) {
        PieChart(
            values = values,
            slice = { i ->
                DefaultSlice(
                    color = colors.getOrElse(i) { Color.Gray },
                    hoverExpandFactor = 1.05f,
                    gap = 2f
                )
            },
            holeSize = 0.5f, // Esto lo convierte en una dona para mostrar el total al centro
//            holeContent = {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text("Total", style = MaterialTheme.typography.labelSmall)
//                    Text(
//                        "$${uiState.totalValue}",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
        )
    }
}