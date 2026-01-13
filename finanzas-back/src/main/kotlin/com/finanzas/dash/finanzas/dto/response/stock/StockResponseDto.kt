package com.finanzas.dash.finanzas.dto.response.stock

import com.finanzas.dash.finanzas.entity.Stock
import java.math.BigDecimal
import java.time.OffsetDateTime

data class StockResponseDto (
    var estado: Boolean = true,
    var message: StockDto
)