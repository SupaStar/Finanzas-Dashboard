package com.frontend.finanzasdashfront.dto.daily_pay

import kotlinx.serialization.Serializable

@Serializable
data class DailyPayResponseDto(
    val id: Long,
    val fixedPortfolioId: Long,
    val amount: Double,
    val anualRateCalculated: Double,
    val payDate: String = ""
)
