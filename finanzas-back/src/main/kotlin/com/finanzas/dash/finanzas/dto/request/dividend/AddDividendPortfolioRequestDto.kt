package com.finanzas.dash.finanzas.dto.request.dividend

import com.finanzas.dash.finanzas.enum.DividendTypeEnum
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class AddDividendPortfolioRequestDto(
    @field:NotNull(message = "El impuesto no puede estar vacio")
    val value: BigDecimal?,
    @field:NotNull(message = "El tipo de dividendo no puede estar vacio")
    val dividendType: DividendTypeEnum?,
    @field:NotNull(message = "La fecha de pago no puede estar vacio")
    val paidDate: String?,
    @field:NotNull(message = "El codigo de moneda no puede estar vacio")
    val currencyCode: String?,
    @field:NotNull(message = "El impuesto no puede estar vacio")
    val tax: BigDecimal?,
    @field:NotNull(message = "El tipo de cambio no puede estar vacio")
    val exchangeRate: BigDecimal?,
)