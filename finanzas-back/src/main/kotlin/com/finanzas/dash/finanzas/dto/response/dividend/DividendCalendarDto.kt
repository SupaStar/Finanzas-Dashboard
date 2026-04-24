package com.finanzas.dash.finanzas.dto.response.dividend

import java.math.BigDecimal

data class DividendCalendarResponseDto(
    val events: List<DividendCalendarEventDto>,
    val summary: DividendCalendarSummaryDto
)

data class DividendCalendarSummaryDto(
    val paidMonth: BigDecimal,
    val expectedMonth: BigDecimal,
    val pendingMonth: BigDecimal
)

data class DividendCalendarEventDto(
    val ticker: String,
    val company: String?,
    val amount: BigDecimal, // Total amount expected or paid
    val type: String, // cash, reinvested, etc.
    val date: String,
    val isPaid: Boolean,
    val dividendId: Long? = null // if paid
)
