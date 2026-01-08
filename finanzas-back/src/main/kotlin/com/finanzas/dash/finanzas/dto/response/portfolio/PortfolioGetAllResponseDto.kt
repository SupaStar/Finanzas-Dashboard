package com.finanzas.dash.finanzas.dto.response.portfolio

data class PortfolioGetAllResponseDto (
    var estado: Boolean = true,
    var messafe: List<PortfolioDto>
)