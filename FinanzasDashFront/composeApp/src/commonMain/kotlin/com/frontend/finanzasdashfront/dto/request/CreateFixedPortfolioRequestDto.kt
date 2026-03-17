package com.frontend.finanzasdashfront.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateFixedPortfolioRequestDto(
    val fixedInstrumentId: Long,
    val amount: Double
)
