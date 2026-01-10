package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.request.LoginRequestDto
import com.frontend.finanzasdashfront.dto.auth.LoginResponseDto
import com.frontend.finanzasdashfront.getPlatform
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthService(private val client: HttpClient) {
    private val login: String = "/auth/login"
    private val register: String = "/auth/register"

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
}