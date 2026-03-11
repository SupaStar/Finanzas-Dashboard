package com.finanzas.dash.finanzas.dto.response.fixed_instrument

import java.math.BigDecimal

data class FixedInstrumentResponseDto(
    val id: Long,
    val name: String,
    val anualRate: BigDecimal
)
