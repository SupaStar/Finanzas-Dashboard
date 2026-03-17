package com.frontend.finanzasdashfront.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFixedPortfolioAmountDto(
    val amount: Float
)
