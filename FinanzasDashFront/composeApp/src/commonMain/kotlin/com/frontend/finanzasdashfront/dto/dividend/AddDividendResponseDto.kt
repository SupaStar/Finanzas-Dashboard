package com.frontend.finanzasdashfront.dto.dividend

import kotlinx.serialization.Serializable

@Serializable
data class AddDividendResponseDto(
    val estado: Boolean = true,
    val message: DividendDto? = null,
    val errors: List<String>? = null
)