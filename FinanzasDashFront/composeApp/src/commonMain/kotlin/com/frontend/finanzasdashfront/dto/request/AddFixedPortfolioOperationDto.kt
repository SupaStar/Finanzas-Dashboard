package com.frontend.finanzasdashfront.dto.request

import com.frontend.finanzasdashfront.enum.FixedPortfolioOperationTypeEnum
import kotlinx.serialization.Serializable

@Serializable
data class AddFixedPortfolioOperationDto(
    val amount: Float,
    val operationType: FixedPortfolioOperationTypeEnum
)
