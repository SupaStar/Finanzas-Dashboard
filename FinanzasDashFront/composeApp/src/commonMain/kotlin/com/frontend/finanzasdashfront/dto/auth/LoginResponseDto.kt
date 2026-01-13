package com.frontend.finanzasdashfront.dto.auth

import com.frontend.finanzasdashfront.dto.ErrorResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(val estado: Boolean, val message: JwtResponseDto? = null, val errors: List<String>? = null,)

@Serializable
data class JwtResponseDto(
    var token: String
)