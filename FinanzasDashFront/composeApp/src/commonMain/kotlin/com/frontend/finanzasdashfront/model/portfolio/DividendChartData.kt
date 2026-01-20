package com.frontend.finanzasdashfront.model.portfolio

import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.utils.month
import com.frontend.finanzasdashfront.utils.year
import kotlinx.datetime.LocalDate

data class DividendChartData(
    val years: List<Int>,
    val monthlyTotalsByYear: Map<Int, List<Float>>
)

fun processDividendData(dividends: List<DividendDto>): DividendChartData {
    val years = dividends.map { it.year() }.distinct().sorted()

    val grouped = dividends.groupBy { it.year() }
    val dataMap = grouped.mapValues { (_, yearDividends) ->
        val monthTotals = FloatArray(12) { 0f }
        yearDividends.forEach { div ->
            val month =div.month() - 1// 0-indexed
            monthTotals[month] += div.value
        }
        monthTotals.toList()
    }

    return DividendChartData(years, dataMap)
}