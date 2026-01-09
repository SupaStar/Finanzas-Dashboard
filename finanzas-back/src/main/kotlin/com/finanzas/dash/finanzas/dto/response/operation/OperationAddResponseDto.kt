package com.finanzas.dash.finanzas.dto.response.operation

data class OperationAddResponseDto(
    val estado: Boolean = true,
    val message: OperationDto
)