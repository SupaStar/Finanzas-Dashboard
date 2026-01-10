package com.frontend.finanzasdashfront.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(val username: String, val password: String, val platformName: String)