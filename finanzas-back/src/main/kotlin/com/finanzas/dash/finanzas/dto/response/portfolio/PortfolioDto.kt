package com.finanzas.dash.finanzas.dto.response.portfolio

import com.finanzas.dash.finanzas.dto.response.dividend.DividendDto
import com.finanzas.dash.finanzas.dto.response.stock.StockDto
import java.math.BigDecimal

data class PortfolioDto(
    var portfolioId: Long,
    var Stock: StockDto,
    var avgPrice: BigDecimal,
    var totalQuantity: BigDecimal,
    var nOperations: Int,
    var generalInformation: List<PortfolioGeneralInformationDto> = emptyList(),
)