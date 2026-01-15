package com.frontend.finanzasdashfront.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(val username: String, val password: String, val platformName: String)