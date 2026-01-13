package com.frontend.finanzasdashfront.dto.operation

import kotlinx.serialization.Serializable

@Serializable
data class GetAllOperationsPortfolioResponseDto(
    var estado: Boolean = true,
    var message: List<OperationDto>,
    val errors: List<String>? = null
)