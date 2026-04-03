package com.frontend.finanzasdashfront.dto.response.statement

import kotlinx.serialization.Serializable

@Serializable
data class StatementResponseDto(
    val id: Long? = null,
    val month: Int,
    val year: Int,
    val downloadUrl: String
)
