package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.fixed_portfolio.FixedPortfolioResponseDto
import com.frontend.finanzasdashfront.dto.fixed_portfolio.FixedPortfolioOperationDto
import com.frontend.finanzasdashfront.dto.request.CreateFixedPortfolioRequestDto
import com.frontend.finanzasdashfront.dto.request.AddFixedPortfolioOperationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class FixedPortfolioService(private val client: HttpClient) {
    private val urlPrefix = "/fixed-portfolio"

    suspend fun getAllByUser(): List<FixedPortfolioResponseDto> {
        val response = client.get("${Constants.BaseUrl}${urlPrefix}") {
            contentType(ContentType.Application.Json)
        }
        
        if (response.status == HttpStatusCode.OK) {
            return response.body<List<FixedPortfolioResponseDto>>()
        } else {
            throw Exception("Error al obtener portafolios fijos: ${response.status}")
        }
    }

    suspend fun create(request: CreateFixedPortfolioRequestDto): FixedPortfolioResponseDto {
        val response = client.post("${Constants.BaseUrl}${urlPrefix}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        
        if (response.status == HttpStatusCode.OK) {
            return response.body<FixedPortfolioResponseDto>()
        } else {
            throw Exception("Error al crear portafolio fijo: ${response.status}")
        }
    }

    suspend fun addOperation(id: Long, request: AddFixedPortfolioOperationDto): FixedPortfolioResponseDto {
        val response = client.post("${Constants.BaseUrl}${urlPrefix}/$id/operations") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (response.status == HttpStatusCode.OK) {
            return response.body<FixedPortfolioResponseDto>()
        } else {
            throw Exception("Error al registrar operación en portafolio fijo: ${response.status}")
        }
    }

    suspend fun getOperations(id: Long): List<FixedPortfolioOperationDto> {
        val response = client.get("${Constants.BaseUrl}${urlPrefix}/$id/operations") {
            contentType(ContentType.Application.Json)
        }

        if (response.status == HttpStatusCode.OK) {
            return response.body<List<FixedPortfolioOperationDto>>()
        } else {
            throw Exception("Error al obtener el historial de operaciones: ${response.status}")
        }
    }

    suspend fun delete(id: Long) {
        val response = client.delete("${Constants.BaseUrl}${urlPrefix}/$id")
        
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Error al eliminar portafolio fijo: ${response.status}")
        }
    }
}
