package com.frontend.finanzasdashfront.dto.fixed_instrument

import kotlinx.serialization.Serializable

@Serializable
data class FixedInstrumentResponseDto(
    val id: Long,
    val name: String,
    val anualRate: Double
)
