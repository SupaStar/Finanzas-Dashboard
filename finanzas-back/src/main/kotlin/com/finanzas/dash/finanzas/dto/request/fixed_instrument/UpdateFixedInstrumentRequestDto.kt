package com.finanzas.dash.finanzas.dto.request.fixed_instrument

import java.math.BigDecimal

data class UpdateFixedInstrumentRequestDto(
    val name: String?,
    val anualRate: BigDecimal?
)
