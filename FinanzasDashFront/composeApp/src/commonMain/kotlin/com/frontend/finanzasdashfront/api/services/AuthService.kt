package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.request.ChangePasswordRequestDto
import com.frontend.finanzasdashfront.dto.request.LoginRequestDto
import com.frontend.finanzasdashfront.dto.auth.ChangePasswordResponseDto
import com.frontend.finanzasdashfront.dto.auth.LoginResponseDto
import com.frontend.finanzasdashfront.dto.request.RegisterRequestDto
import com.frontend.finanzasdashfront.getPlatform
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthService(private val client: HttpClient) {
    private val login: String = "/auth/login"
    private val register: String = "/auth/register"
    private val changePasswordUrl: String = "/auth/change-password"

    suspend fun login(user: String, pass: String): LoginResponseDto {
        val response = client.post("${Constants.BaseUrl}${login}") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(user, pass, getPlatform().name))
        }

        val responseBody = response.body<LoginResponseDto>()

        if (response.status == HttpStatusCode.OK) {
            return responseBody
        } else {
            val errorMsg = responseBody.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }

    suspend fun register(requestDto: RegisterRequestDto): LoginResponseDto {
        val response = client.post("${Constants.BaseUrl}${register}") {
            contentType(ContentType.Application.Json)
            setBody(requestDto)
        }

        val responseBody = response.body<LoginResponseDto>()

        if (response.status == HttpStatusCode.OK) {
            return responseBody
        } else {
            val errorMsg = responseBody.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        val response = client.put("${Constants.BaseUrl}${changePasswordUrl}") {
            contentType(ContentType.Application.Json)
            setBody(ChangePasswordRequestDto(currentPassword, newPassword, confirmPassword))
        }

        val responseBody = response.body<ChangePasswordResponseDto>()
        if (response.status != HttpStatusCode.OK || !responseBody.estado) {
            val errorMsg = responseBody.errors?.joinToString("\n") ?: "Error al cambiar la contraseña"
            throw Exception(errorMsg)
        }
    }
}