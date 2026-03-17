package com.finanzas.dash.finanzas.dto.response

data class ChangePasswordResponseDto(
    val estado: Boolean,
    val message: String? = null,
    val errors: List<String>? = null
)
