package com.frontend.finanzasdashfront.dto.stock

import com.frontend.finanzasdashfront.dto.operation.MessageOperationResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class GetAllStocksResponseDto(
    var estado: Boolean = true,
    var message: MessageOperationResponseDto? = null,
    val errors: List<String>? = null
)