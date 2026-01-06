package com.finanzas.dash.finanzas.dto.response

data class AuthResponseDto<T>(
    var estado: Boolean = true,
    var message: List<T> = emptyList(),
    var error: List<T> = emptyList()
)