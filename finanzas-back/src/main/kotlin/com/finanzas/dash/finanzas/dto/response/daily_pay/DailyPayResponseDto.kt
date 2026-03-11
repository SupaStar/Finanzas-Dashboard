package com.finanzas.dash.finanzas.dto.response.daily_pay

import java.math.BigDecimal

data class DailyPayResponseDto(
    val id: Long,
    val fixedPortfolioId: Long,
    val amount: BigDecimal,
    val anualRateCalculated: BigDecimal
)
