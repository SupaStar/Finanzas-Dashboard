package com.finanzas.dash.finanzas.dto.request.stocks

import jakarta.validation.constraints.NotEmpty
import java.math.BigInteger

data class AddStockRequestDto(
    @field:NotEmpty(message = "La accion no puede estar vacia")
    val stockName: String?,
    val brokerId: Long
)