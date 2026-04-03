package com.finanzas.dash.finanzas.dto.response.statement

data class StatementResponseDto(
    val id: Long?,
    val month: Int,
    val year: Int,
    val downloadUrl: String
)
