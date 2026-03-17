package com.frontend.finanzasdashfront.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordResponseDto(
    val estado: Boolean,
    val message: String? = null,
    val errors: List<String>? = null
)
