package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.request.auth.LoginRequestDto
import com.finanzas.dash.finanzas.dto.request.auth.RegisterRequestDto
import com.finanzas.dash.finanzas.dto.response.AuthResponseDto
import com.finanzas.dash.finanzas.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {
    @PostMapping("/register")
    fun register(
        @Validated @RequestBody request: RegisterRequestDto,
        requestData: HttpServletRequest
    ): ResponseEntity<AuthResponseDto> {
        request.agent = requestData.getHeader("User-Agent")
        val response = authService.register(request)
        return ResponseEntity(response, org.springframework.http.HttpStatus.OK)
    }

    @PostMapping("/login")
    fun login(
        @Validated @RequestBody request: LoginRequestDto,
        requestData: HttpServletRequest
    ): ResponseEntity<AuthResponseDto> {
        request.agent = requestData.getHeader("User-Agent")
        val response = authService.login(request)
        return ResponseEntity(response, org.springframework.http.HttpStatus.OK)
    }
}
