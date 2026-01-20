package com.frontend.finanzasdashfront.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.model.portfolio.DividendChartData
import com.frontend.finanzasdashfront.model.portfolio.processDividendData
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.bar.BarPlotGroupedPointEntry
import io.github.koalaplot.core.bar.DefaultBar
import io.github.koalaplot.core.bar.GroupedVerticalBarPlot
import io.github.koalaplot.core.legend.FlowLegend
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.generateHueColorPalette
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.bar.BarPosition
import io.github.koalaplot.core.bar.DefaultBarPosition


private val MonthNames = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun DividendBarChart(dividends: List<DividendDto>) {
    val processedData = remember(dividends) { processDividendData(dividends) }
    val colors = remember(processedData.years) { generateHueColorPalette(processedData.years.size) }
    val maxVal = processedData.monthlyTotalsByYear.values.flatten().maxOrNull() ?: 100f

    Column {
        ChartLayout(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            title = { Text("Distribución de Dividendos por Mes", style = MaterialTheme.typography.titleLarge) },
            legend = {
                FlowLegend(
                    itemCount = processedData.years.size,
                    symbol = { i -> Symbol(modifier = Modifier.size(12.dp), fillBrush = SolidColor(colors[i])) },
                    label = { i -> Text(processedData.years[i].toString()) }
                )
            },
            legendLocation = LegendLocation.BOTTOM
        ) {
            XYGraph(
                xAxisModel = CategoryAxisModel(MonthNames),
                yAxisModel = FloatLinearAxisModel(0f..maxVal * 1.1f), // 10% de margen arriba
                xAxisTitle = "Meses",
                yAxisTitle = "Monto Total",
                content = {
                    GroupedVerticalBarPlot(
                        data = barChartEntries(processedData),
                        bar = { _, groupIndex, _ ->
                            DefaultBar(
                                brush = SolidColor(colors[groupIndex]),
                                modifier = Modifier.sizeIn(maxWidth = 20.dp)
                            )
                        }
                    )
                }
            )
        }
    }
}

private fun barChartEntries(data: DividendChartData): List<BarPlotGroupedPointEntry<String, Float>> {
    // Iteramos por mes (el eje X)
    return MonthNames.mapIndexed { monthIndex, monthName ->
        object : BarPlotGroupedPointEntry<String, Float> {
            override val i: String = monthName
            override val d: List<BarPosition<Float>> = data.years.map { year ->
                val value = data.monthlyTotalsByYear[year]?.get(monthIndex) ?: 0f
                DefaultBarPosition(0f, value)
            }
        }
    }
}