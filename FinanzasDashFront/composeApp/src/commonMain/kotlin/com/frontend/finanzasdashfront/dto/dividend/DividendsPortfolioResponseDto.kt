package com.frontend.finanzasdashfront.dto.dividend

import kotlinx.serialization.Serializable

@Serializable
data class DividendsPortfolioResponseDto(
    val estado: Boolean = true,
    val message: MessageDividendsPortfolioResponseDto,
    val errors: List<String>? = null
)