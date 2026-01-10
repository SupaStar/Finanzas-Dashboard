package com.frontend.finanzasdashfront.dto.portfolio

import com.frontend.finanzasdashfront.dto.stock.StockDto
import kotlinx.serialization.Serializable

@Serializable
data class PortfolioDto(
    var portfolioId: Long,
    var Stock: StockDto,
    var avgPrice: Float,
    var totalQuantity: Float
)