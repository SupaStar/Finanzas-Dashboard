package com.frontend.finanzasdashfront.dto.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class PortfolioGetAllResponseDto(
    var estado: Boolean,
    var messafe: List<PortfolioDto>,
    val errors: List<String>? = null
)