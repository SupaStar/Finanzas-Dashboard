package com.finanzas.dash.finanzas.dto.response.fixed_portfolio

import com.finanzas.dash.finanzas.enum.FixedPortfolioOperationTypeEnum
import java.math.BigDecimal

data class FixedPortfolioOperationDto(
    val id: Long,
    val amount: BigDecimal,
    val operationType: FixedPortfolioOperationTypeEnum,
    val operationDate: String
)
