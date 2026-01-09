package com.finanzas.dash.finanzas.dto.response.operation


data class OperationsPortfolioResponseDto (
    var estado: Boolean = true,
    var message: List<OperationDto>
)