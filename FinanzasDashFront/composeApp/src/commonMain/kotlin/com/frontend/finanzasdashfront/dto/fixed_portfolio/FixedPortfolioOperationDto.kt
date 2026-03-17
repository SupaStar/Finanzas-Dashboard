package com.frontend.finanzasdashfront.dto.fixed_portfolio

import com.frontend.finanzasdashfront.enum.FixedPortfolioOperationTypeEnum
import kotlinx.serialization.Serializable

@Serializable
data class FixedPortfolioOperationDto(
    val id: Long,
    val amount: Float,
    val operationType: FixedPortfolioOperationTypeEnum,
    val operationDate: String
)
