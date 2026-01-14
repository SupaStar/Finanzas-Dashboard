package com.frontend.finanzasdashfront.dto.operation

import kotlinx.serialization.Serializable

@Serializable
data class GetAllOperationsPortfolioResponseDto(
    var estado: Boolean = true,
    var message: MessageOperationResponseDto? = null,
    val errors: List<String>? = null
)