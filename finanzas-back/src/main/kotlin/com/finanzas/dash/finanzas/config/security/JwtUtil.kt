package com.finanzas.dash.finanzas.config.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import javax.crypto.SecretKey
import io.jsonwebtoken.security.Keys
import java.util.Date

@Component
class JwtUtil(private val jwtConfigValues: JwtConfigValues) {
    private val key: SecretKey = Keys.hmacShaKeyFor(jwtConfigValues.secret.toByteArray())
    private val expirationMs: Long = jwtConfigValues.expiration * 15 // 15 minutos

    fun generateToken(id: Int): String {
        return Jwts.builder()
            .subject(id.toString())
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload.subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}