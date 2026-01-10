package com.frontend.finanzasdashfront.dto.stock

import kotlinx.serialization.Serializable

@Serializable
data class GetAllStocksResponseDto(
    var estado: Boolean = true,
    var message: List<StockDto>,
    val errors: List<String>? = null
)