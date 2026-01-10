package com.frontend.finanzasdashfront.dto.stock

import kotlinx.serialization.Serializable

@Serializable
data class AddStockResponseDto(
    var estado: Boolean = true,
    var message: StockDto,
    val errors: List<String>? = null
)