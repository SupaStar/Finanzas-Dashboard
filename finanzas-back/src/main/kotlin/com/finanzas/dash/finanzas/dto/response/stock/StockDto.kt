package com.finanzas.dash.finanzas.dto.response.stock

import java.math.BigDecimal
import java.time.OffsetDateTime

data class StockDto(
    var stockId: Long,
    var name: String,
    var symbol: String,
    var broker: String,
    var closeDay: BigDecimal,
    var lastFetch: OffsetDateTime,
    var currency: String
)