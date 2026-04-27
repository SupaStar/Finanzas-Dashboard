package com.frontend.finanzasdashfront.dto.dividend

import kotlinx.serialization.Serializable

@Serializable
data class DividendCalendarResponseDto(
    val events: List<DividendCalendarEventDto>,
    val summary: DividendCalendarSummaryDto
)

@Serializable
data class DividendCalendarSummaryDto(
    val paidMonth: Double,
    val expectedMonth: Double,
    val pendingMonth: Double
)

@Serializable
data class DividendCalendarEventDto(
    val ticker: String,
    val company: String? = null,
    val amount: Double,
    val type: String,
    val date: String,
    val isPaid: Boolean,
    val dividendId: Long? = null
)
