package com.finanzas.dash.finanzas.dto.response.dividend

data class AddDividendResponseDto(
    val estado: Boolean = true,
    val message: DividendDto
)