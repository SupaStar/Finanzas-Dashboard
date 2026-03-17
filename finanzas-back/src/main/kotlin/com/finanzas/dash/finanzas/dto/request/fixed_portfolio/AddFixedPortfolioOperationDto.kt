package com.finanzas.dash.finanzas.dto.request.fixed_portfolio

import com.finanzas.dash.finanzas.enum.FixedPortfolioOperationTypeEnum
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class AddFixedPortfolioOperationDto(
    @field:NotNull(message = "El monto es requerido")
    @field:DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    val amount: BigDecimal?,

    @field:NotNull(message = "El tipo de operación es requerido")
    val operationType: FixedPortfolioOperationTypeEnum?
)
