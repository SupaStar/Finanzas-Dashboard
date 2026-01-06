package com.finanzas.dash.finanzas.dto.response

import com.finanzas.dash.finanzas.entity.AuthDevice

data class RegisterResponseDto(
    var estado: Boolean = true,
    var message: JwtResponseDto
)

data class JwtResponseDto(
    var token: String,
    var device: AuthDevice
)