package com.frontend.finanzasdashfront.dto.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class PortfolioResponseDto(
    var estado: Boolean,
    var messafe: PortfolioDto,
    val errors: List<String>? = null
)