package com.finanzas.dash.finanzas.dto.request.fixed_portfolio

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class UpdateFixedPortfolioAmountDto(
    @field:NotNull(message = "El monto no puede estar vacio")
    val amount: BigDecimal?
)
