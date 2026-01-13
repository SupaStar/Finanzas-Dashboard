package com.finanzas.dash.finanzas.dto.response.operation

import com.finanzas.dash.finanzas.dto.response.stock.StockDto


data class OperationsPortfolioResponseDto (
    var estado: Boolean = true,
    var message: MessageOperationResponseDto
)

data class MessageOperationResponseDto (
    var stock: StockDto,
    val operations: List<OperationDto>
)