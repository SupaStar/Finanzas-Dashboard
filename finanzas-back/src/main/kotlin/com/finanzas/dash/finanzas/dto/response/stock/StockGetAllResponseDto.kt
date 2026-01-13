package com.finanzas.dash.finanzas.dto.response.stock

data class StockGetAllResponseDto (
    var estado: Boolean = true,
    var message: List<StockDto>
)