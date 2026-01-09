package com.finanzas.dash.finanzas.dto.request.auth

import com.finanzas.dash.finanzas.enum.ProviderEnum
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class LoginRequestDto(
    @field:NotNull(message = "El campo usuario no puede estar vacío")
    val username: String?,
    @field:NotNull(message = "El campo password no puede estar vacío")
    val password: String?,

    var platform_name: String?,
)