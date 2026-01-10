package com.frontend.finanzasdashfront.dto.auth

import com.frontend.finanzasdashfront.Platform
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(val username: String, val password: String, val platformName: String)