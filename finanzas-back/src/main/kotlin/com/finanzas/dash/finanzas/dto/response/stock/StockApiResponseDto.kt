package com.finanzas.dash.finanzas.dto.response.stock

import java.math.BigDecimal
import java.math.BigInteger

data class StockApiResponseDto(
    var ticker: String,
    var cached: Boolean,
    var data: StockData
)

data class StockData(
    var symbol: String,
    var name: String,
    var price: BigDecimal,
    var currency: String,
    var marketCap: String?,
    var sector: String?
)

