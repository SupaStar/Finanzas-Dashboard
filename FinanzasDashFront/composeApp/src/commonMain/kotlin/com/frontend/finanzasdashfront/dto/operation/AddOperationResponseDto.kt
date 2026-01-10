package com.frontend.finanzasdashfront.dto.operation

import kotlinx.serialization.Serializable

@Serializable
class AddOperationResponseDto(
    val estado: Boolean = true,
    val message: OperationDto,
    val errors: List<String>? = null
)