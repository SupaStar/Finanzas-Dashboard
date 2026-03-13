package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.fixed_portfolio.FixedPortfolioResponseDto
import com.frontend.finanzasdashfront.dto.request.CreateFixedPortfolioRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
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
}
