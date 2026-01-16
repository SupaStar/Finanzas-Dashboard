package com.frontend.finanzasdashfront.dto.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class PortfolioResponseDto(
    var estado: Boolean,
    var message: PortfolioDto? = null,
    val errors: List<String>? = null
)