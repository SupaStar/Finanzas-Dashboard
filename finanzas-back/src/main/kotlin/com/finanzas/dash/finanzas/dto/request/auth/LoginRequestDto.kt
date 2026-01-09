package com.finanzas.dash.finanzas.dto.request.auth

import com.finanzas.dash.finanzas.enum.ProviderEnum
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class LoginRequestDto(
    @field:NotNull(message = "El campo usuario no puede estar vacío")
    val username: String?,
    @field:NotNull(message = "El campo password no puede estar vacío")
    val password: String?,
    @field:NotNull(message = "La ip no puede estar vacia")
    val ip: String?,
    @field:NotNull(message = "El dispositivo no puede estar vacio")
    val device: String?,

    var agent: String?,
)