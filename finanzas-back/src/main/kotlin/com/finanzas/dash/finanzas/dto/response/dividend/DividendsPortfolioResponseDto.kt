package com.finanzas.dash.finanzas.dto.response.dividend

data class DividendsPortfolioResponseDto(
    val estado: Boolean = true,
    val message: List<DividendDto>
)