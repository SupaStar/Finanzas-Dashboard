package com.frontend.finanzasdashfront.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto (
    val errors: List<String>
)