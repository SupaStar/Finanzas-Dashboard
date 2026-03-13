package com.finanzas.dash.finanzas.dto.request.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChangePasswordRequestDto(
    @field:NotBlank(message = "La contraseña actual no puede estar vacía")
    val currentPassword: String,

    @field:NotBlank(message = "La nueva contraseña no puede estar vacía")
    @field:Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
    val newPassword: String,

    @field:NotBlank(message = "La confirmación de contraseña no puede estar vacía")
    val newPasswordConfirmation: String
)
