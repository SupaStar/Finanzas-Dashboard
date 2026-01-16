package com.frontend.finanzasdashfront.dto.dividend

import com.frontend.finanzasdashfront.dto.enums.DividendTypeEnum
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioDto
import kotlinx.serialization.Serializable

@Serializable
data class DividendDto(
    val dividendId: Long,
    val dividendType: DividendTypeEnum,
    val value: Float,
    val portfolio: PortfolioDto,
    val paidDate: String,
    val currencyCode: String,
    val tax: Float,
    val netValue: Float,
    val exchangeRate: Float,
)