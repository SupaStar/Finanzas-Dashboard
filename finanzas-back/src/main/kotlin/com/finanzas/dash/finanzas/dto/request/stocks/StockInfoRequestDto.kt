package com.finanzas.dash.finanzas.dto.request.stocks

import jakarta.validation.constraints.NotEmpty

data class StockInfoRequestDto(
    @field:NotEmpty(message = "La lista de acciones no puede estar vacia")
    var stocks: List<String>
)