package com.frontend.finanzasdashfront.api.auth

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.auth.LoginRequestDto
import com.frontend.finanzasdashfront.dto.auth.LoginResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class AuthService(private val client: HttpClient) {
    val login: String = "/auth/login"
    var register: String = "/auth/register"

    suspend fun login(user: String, pass: String): LoginResponseDto {
        val response = client.post("${Constants.BaseUrl}${login}") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(user, pass))
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