package com.frontend.finanzasdashfront.dto.operation

import com.frontend.finanzasdashfront.dto.stock.StockDto
import kotlinx.serialization.Serializable

@Serializable
data class MessageOperationResponseDto (
    var stock: StockDto,
    val operations: List<OperationDto>
)