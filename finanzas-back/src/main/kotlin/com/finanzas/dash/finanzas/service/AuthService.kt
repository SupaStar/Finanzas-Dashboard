package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.Encrypter
import com.finanzas.dash.finanzas.config.exception.BadRequestException
import com.finanzas.dash.finanzas.config.security.JwtUtil
import com.finanzas.dash.finanzas.dto.request.auth.RegisterRequestDto
import com.finanzas.dash.finanzas.dto.response.JwtResponseDto
import com.finanzas.dash.finanzas.dto.response.RegisterResponseDto
import com.finanzas.dash.finanzas.entity.AuthDevice
import com.finanzas.dash.finanzas.entity.User
import com.finanzas.dash.finanzas.entity.UserAuth
import com.finanzas.dash.finanzas.repository.AuthDeviceRepository
import com.finanzas.dash.finanzas.repository.UserAuthRepository
import com.finanzas.dash.finanzas.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.InetAddress
import java.time.OffsetDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val userAuthRepository: UserAuthRepository,
    private val authDeviceRepository: AuthDeviceRepository,
    private val encrypter: Encrypter,
    private val jwtUtil: JwtUtil
) {
    @Transactional
    fun register(request: RegisterRequestDto): RegisterResponseDto {
        if (request.password != request.password_confirmation) {
            throw BadRequestException(listOf("Las contraseñas no coinciden"), HttpStatus.BAD_REQUEST)
        }
        val user = try {
            userRepository.save(
                User().apply {
                    username = request.username
                })
        } catch (ex: DataIntegrityViolationException) {
            throw BadRequestException(listOf("Usuario en uso"), HttpStatus.CONFLICT)
        }

        val userAuth = userAuthRepository.save(
            UserAuth().apply {
                this.user = user
                this.passwordHash = encrypter.encrypt(request.password)
                this.provider = request.provider
                this.lastLoginAt = OffsetDateTime.now()
                this.passwordUpdatedAt = OffsetDateTime.now()
            });

        val authDevice = authDeviceRepository.save(
            AuthDevice().apply {
                this.auth = userAuth
                this.deviceId = request.device
                this.lastUsedAt = OffsetDateTime.now()
                this.ip = InetAddress.getByName(request.ip)
                this.userAgent = request.agent
            })
        val jwt = jwtUtil.generateToken(user.userId!!)
        return RegisterResponseDto(true, JwtResponseDto(jwt, authDevice))
    }
}