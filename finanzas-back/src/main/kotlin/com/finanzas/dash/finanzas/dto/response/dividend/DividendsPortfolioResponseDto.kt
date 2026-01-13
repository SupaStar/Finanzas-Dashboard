package com.finanzas.dash.finanzas.dto.response.dividend

import com.finanzas.dash.finanzas.dto.response.stock.StockDto

data class DividendsPortfolioResponseDto(
    val estado: Boolean = true,
    val message: MessageDividendsPortfolioResponseDto
)

data class MessageDividendsPortfolioResponseDto(
    val stock: StockDto,
    val dividends: List<DividendDto>
)