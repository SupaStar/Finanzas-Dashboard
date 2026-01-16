package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.broker.GetAllBrokerResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class BrokerService(private val client: HttpClient) {
    private val allBrokerUrl = "/broker/all"
    suspend fun getBrokers(): GetAllBrokerResponseDto {
        val response = client.get("${Constants.BaseUrl}${allBrokerUrl}") {
            contentType(ContentType.Application.Json)
        }
        val responseDto = response.body<GetAllBrokerResponseDto>()

        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }
}