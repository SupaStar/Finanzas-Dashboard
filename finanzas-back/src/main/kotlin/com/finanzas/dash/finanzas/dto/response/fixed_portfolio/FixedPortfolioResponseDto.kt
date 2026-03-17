package com.finanzas.dash.finanzas.dto.response.fixed_portfolio

import com.finanzas.dash.finanzas.dto.response.fixed_instrument.FixedInstrumentResponseDto
import java.math.BigDecimal

data class FixedPortfolioResponseDto(
    val id: Long,
    val fixedInstrument: FixedInstrumentResponseDto,
    val amount: BigDecimal
)
