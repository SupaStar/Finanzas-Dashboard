package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioResponseDto
import com.frontend.finanzasdashfront.dto.stock.AddStockResponseDto
import com.frontend.finanzasdashfront.dto.stock.GetAllStocksResponseDto
import com.frontend.finanzasdashfront.request.AddStockPortfolioRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class StockService(private val client: HttpClient) {
    private val getAll = "/stock/all"
    private val add = "/stock/add"
    suspend fun getPortfolio(): GetAllStocksResponseDto {
        val response = client.get("${Constants.BaseUrl}${getAll}") {
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
    //TODO agregar request
    suspend fun addStockToPortfolio(request: AddStockPortfolioRequestDto): AddStockResponseDto {
        val response = client.post("${Constants.BaseUrl}${add}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val responseDto = response.body<AddStockResponseDto>()
        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }
}