package com.frontend.finanzasdashfront.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequestDto(
    val currentPassword: String,
    val newPassword: String,
    val newPasswordConfirmation: String
)
