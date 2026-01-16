package com.frontend.finanzasdashfront.dto.dividend

import com.frontend.finanzasdashfront.dto.stock.StockDto
import kotlinx.serialization.Serializable

@Serializable
data class MessageDividendsPortfolioResponseDto(
    val stock: StockDto,
    val dividends: List<DividendDto>
)