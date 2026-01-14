package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.operation.AddOperationResponseDto
import com.frontend.finanzasdashfront.dto.operation.GetAllOperationsPortfolioResponseDto
import com.frontend.finanzasdashfront.dto.request.AddOperationRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class OperationService(private val client: HttpClient) {
    private val operationsPortfolioUrl = "/operation/all"
    private val operationsPortfolioAddUrl = "/operation/add"
    suspend fun getAllOperations(idPortfolio: Long): GetAllOperationsPortfolioResponseDto {
        val response = client.get("${Constants.BaseUrl}${operationsPortfolioUrl}/${idPortfolio}") {
            contentType(ContentType.Application.Json)
        }
        val responseDto = response.body<GetAllOperationsPortfolioResponseDto>()

        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }

    suspend fun addOperation(request: AddOperationRequestDto): AddOperationResponseDto {
        val response = client.post("${Constants.BaseUrl}${operationsPortfolioAddUrl}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val responseDto = response.body<AddOperationResponseDto>()
        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error desconocido: $errorMsg")
        }
    }
}