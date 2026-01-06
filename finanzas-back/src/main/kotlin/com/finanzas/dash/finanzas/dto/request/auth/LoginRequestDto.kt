package com.finanzas.dash.finanzas.dto.request.auth

import com.finanzas.dash.finanzas.enum.ProviderEnum
import jakarta.validation.constraints.NotEmpty

data class LoginRequestDto(
    @field:NotEmpty(message = "El campo usuario no puede estar vacío")
    val username: String,
    @field:NotEmpty(message = "El campo password no puede estar vacío")
    val password: String,
    @field:NotEmpty(message = "La ip no puede estar vacia")
    val ip: String,
    @field:NotEmpty(message = "El dispositivo no puede estar vacio")
    val device: String,

    var agent: String?,
)