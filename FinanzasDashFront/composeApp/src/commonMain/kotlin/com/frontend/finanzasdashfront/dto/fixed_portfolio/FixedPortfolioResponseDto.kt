package com.frontend.finanzasdashfront.dto.fixed_portfolio

import com.frontend.finanzasdashfront.dto.fixed_instrument.FixedInstrumentResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class FixedPortfolioResponseDto(
    val id: Long,
    val fixedInstrument: FixedInstrumentResponseDto,
    val amount: Double
)
