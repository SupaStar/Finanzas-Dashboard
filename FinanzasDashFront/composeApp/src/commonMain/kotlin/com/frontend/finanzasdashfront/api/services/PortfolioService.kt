package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioGetAllResponseDto
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioResponseDto
import com.frontend.finanzasdashfront.request.AddStockPortfolioRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

import io.ktor.http.*

class PortfolioService(private val client: HttpClient) {
    private val urlGetPortfolio = "/portfolio/get"
    private val urlAddStockPortfolio = "/portfolio/add"
    suspend fun getPortfolio(): PortfolioGetAllResponseDto {
        val response = client.get("${Constants.BaseUrl}${urlGetPortfolio}") {
            contentType(ContentType.Application.Json)
        }
        val responseDto = response.body<PortfolioGetAllResponseDto>()

        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }

    suspend fun addStockToPortfolio(request: AddStockPortfolioRequestDto): PortfolioResponseDto {
        val response = client.post("${Constants.BaseUrl}${urlAddStockPortfolio}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val responseDto = response.body<PortfolioResponseDto>()
        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }
}