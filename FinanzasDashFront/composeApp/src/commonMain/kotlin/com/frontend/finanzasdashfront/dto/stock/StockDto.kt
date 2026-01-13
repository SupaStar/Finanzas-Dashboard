package com.frontend.finanzasdashfront.dto.stock

import kotlinx.serialization.Serializable

@Serializable
data class StockDto(
    var stockId: Long,
    var name: String,
    var symbol: String,
    var broker: String,
    var closeDay: Float,
    var lastFetch: String,
    var currency: String
)