package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.entity.User
import com.finanzas.dash.finanzas.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityService(private val userRepository: UserRepository) {

    fun currentUser(): User {
        val authentication =
            SecurityContextHolder.getContext().authentication ?: throw RuntimeException("No autenticado")
        val userId = authentication.name

        return userRepository.findByUserId(userId.toInt())!!
    }
}