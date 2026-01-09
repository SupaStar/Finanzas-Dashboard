package com.finanzas.dash.finanzas.dto.response.dividend

import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioDto
import com.finanzas.dash.finanzas.enum.DividendTypeEnum
import java.math.BigDecimal
import java.time.OffsetDateTime

data class DividendDto(
    val dividendId: Long,
    val dividendType: DividendTypeEnum,
    val value: BigDecimal,
    val portfolio: PortfolioDto,
    val paidDate: OffsetDateTime,
    val currencyCode: String,
    val tax: BigDecimal,
    val netValue: BigDecimal,
    val exchangeRate: BigDecimal,
)