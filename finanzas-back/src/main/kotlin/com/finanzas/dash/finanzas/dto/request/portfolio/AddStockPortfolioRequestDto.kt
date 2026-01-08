package com.finanzas.dash.finanzas.dto.request.portfolio

import jakarta.validation.constraints.NotNull

data class AddStockPortfolioRequestDto (
    @field:NotNull(message = "La accion no puede estar vacia")
    val stockID: Long?
)