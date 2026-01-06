package com.finanzas.dash.finanzas.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class JwtConfigValues(
    @Value("\${jwt.sign}") val secret: String,
    @Value("\${jwt.expiration}") val expiration: Long,
)