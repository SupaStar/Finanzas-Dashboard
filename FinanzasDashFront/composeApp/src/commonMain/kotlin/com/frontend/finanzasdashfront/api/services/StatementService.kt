package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.response.statement.StatementResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.*

class StatementService(private val client: HttpClient) {
    private val urlGetUserStatements = "/api/statement/user"

    suspend fun getUserStatements(): List<StatementResponseDto> {
        val response = client.get("${Constants.BaseUrl}${urlGetUserStatements}") {
            contentType(ContentType.Application.Json)
        }
        
        if (response.status == HttpStatusCode.OK) {
            return response.body<List<StatementResponseDto>>()
        } else {
            throw Exception("Error al obtener los estados de cuenta (HTTP \${response.status.value})")
        }
    }
}
