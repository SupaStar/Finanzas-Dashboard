package com.frontend.finanzasdashfront.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioGeneralInformationDto
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.bar.BarPlotGroupedPointEntry
import io.github.koalaplot.core.bar.BarPosition
import io.github.koalaplot.core.bar.DefaultBar
import io.github.koalaplot.core.bar.DefaultBarPosition
import io.github.koalaplot.core.bar.GroupedVerticalBarPlot
import io.github.koalaplot.core.legend.FlowLegend
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.generateHueColorPalette
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph

private val MonthNames = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

data class ProcessedGeneralInfoData(
    val years: List<Int>,
    val monthlyTotalsByYear: Map<Int, FloatArray>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProcessedGeneralInfoData) return false

        if (years != other.years) return false
        if (monthlyTotalsByYear.size != other.monthlyTotalsByYear.size) return false

        for ((key, value) in monthlyTotalsByYear) {
            val otherValue = other.monthlyTotalsByYear[key]
            if (otherValue == null || !value.contentEquals(otherValue)) {
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = years.hashCode()
        result = 31 * result + monthlyTotalsByYear.hashCode()
        return result
    }
}

private fun processGeneralInfoData(generalInfo: List<PortfolioGeneralInformationDto>): ProcessedGeneralInfoData {
    val groupedByYear = generalInfo.groupBy { it.year }
    val years = groupedByYear.keys.sorted()
    val monthlyTotalsByYear = mutableMapOf<Int, FloatArray>()

    for (year in years) {
        val yearData = groupedByYear[year] ?: emptyList()
        val monthTotals = FloatArray(12) { 0f }
        
        // Sumar los dividendos por mes (si hay varias acciones en el portafolio en el mismo mes y año)
        for (info in yearData) {
            val monthIndex = info.month - 1
            if (monthIndex in 0..11) {
                monthTotals[monthIndex] += info.dividendsTotal
            }
        }
        monthlyTotalsByYear[year] = monthTotals
    }

    return ProcessedGeneralInfoData(years, monthlyTotalsByYear)
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun GeneralInfoBarChart(generalInfo: List<PortfolioGeneralInformationDto>) {
    val processedData = remember(generalInfo) { processGeneralInfoData(generalInfo) }
    val colors = remember(processedData.years) { generateHueColorPalette(processedData.years.size) }
    
    // Obtener el valor máximo para configurar el eje Y correctamente, usando 100f por defecto si no hay data
    val maxVal = processedData.monthlyTotalsByYear.values.flatMap { it.toList() }.maxOrNull() ?: 100f
    val yAxisMax = if (maxVal <= 0f) 100f else maxVal * 1.1f

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
                yAxisModel = FloatLinearAxisModel(0f..yAxisMax), // 10% de margen arriba
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

private fun barChartEntries(data: ProcessedGeneralInfoData): List<BarPlotGroupedPointEntry<String, Float>> {
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
