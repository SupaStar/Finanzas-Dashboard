package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.stock.GetAllStocksResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class OperationService(private val client: HttpClient) {
    private val operationsPortfolioUrl = "/operation/all"
    suspend fun getAllOperations(idPortfolio: Long): GetAllStocksResponseDto {
        val response = client.get("${Constants.BaseUrl}${operationsPortfolioUrl}/${idPortfolio}") {
            contentType(ContentType.Application.Json)
        }
        val responseDto = response.body<GetAllStocksResponseDto>()

        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }
}