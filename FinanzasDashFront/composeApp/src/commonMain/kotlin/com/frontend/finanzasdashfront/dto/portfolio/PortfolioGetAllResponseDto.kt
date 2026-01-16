package com.frontend.finanzasdashfront.dto.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class PortfolioGetAllResponseDto(
    var estado: Boolean,
    var message: List<PortfolioDto>,
    var usdPrice: Float,
    val errors: List<String>? = null
)