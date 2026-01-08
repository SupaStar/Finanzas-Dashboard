package com.finanzas.dash.finanzas.dto.response.portfolio

import com.finanzas.dash.finanzas.dto.response.stock.StockDto
import java.math.BigDecimal

data class PortfolioResponseDto(
    var estado: Boolean = true,
    var messafe: PortfolioDto
)

data class PortfolioDto(
    var portfolioId: Long,
    var Stock: StockDto,
    var avgPrice: BigDecimal,
    var totalQuantity: BigDecimal
)