package com.finanzas.dash.finanzas.dto.request.fixed_instrument

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CreateFixedInstrumentRequestDto(
    @field:NotBlank(message = "El nombre no puede estar vacio")
    val name: String?,
    
    @field:NotNull(message = "La tasa anual no puede estar vacia")
    val anualRate: BigDecimal?
)
