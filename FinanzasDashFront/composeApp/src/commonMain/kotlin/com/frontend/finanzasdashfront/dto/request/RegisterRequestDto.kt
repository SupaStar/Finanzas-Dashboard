package com.frontend.finanzasdashfront.dto.request

import com.frontend.finanzasdashfront.dto.enums.ProviderEnum
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto (
    val username: String,
    val password: String,
    val password_confirmation: String,

    val provider: ProviderEnum = ProviderEnum.local,
    var platform_name : String,
)