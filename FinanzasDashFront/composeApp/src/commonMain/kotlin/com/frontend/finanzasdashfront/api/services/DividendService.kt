package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.dividend.AddDividendResponseDto
import com.frontend.finanzasdashfront.dto.dividend.DividendsPortfolioResponseDto
import com.frontend.finanzasdashfront.dto.request.AddDividendPortfolioRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class DividendService(private val client: HttpClient) {
    private val dividendsPortfolioUrl = "/dividend/all"
    private val dividendAddUrl = "/dividend/add"
    private val dividendDeleteUrl = "/dividend/delete"
    
    suspend fun getDividends(idPortfolio: Long): DividendsPortfolioResponseDto {
        val response = client.get("${Constants.BaseUrl}${dividendsPortfolioUrl}/${idPortfolio}") {
            contentType(ContentType.Application.Json)
        }
        val responseDto = response.body<DividendsPortfolioResponseDto>()

        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }

    suspend fun addDividend(idPortfolio: Long, request: AddDividendPortfolioRequestDto): AddDividendResponseDto {
        val response = client.post("${Constants.BaseUrl}${dividendAddUrl}/${idPortfolio}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val responseDto = response.body<AddDividendResponseDto>()
        if (response.status == HttpStatusCode.OK) {
            return responseDto
        } else {
            val errorMsg = responseDto.errors?.joinToString("\n") ?: "Error desconocido"
            throw Exception("Error de autenticación: $errorMsg")
        }
    }

    suspend fun deleteDividend(dividendId: Long) {
        val response = client.delete("${Constants.BaseUrl}${dividendDeleteUrl}/${dividendId}")
        if (response.status != HttpStatusCode.OK && response.status != HttpStatusCode.NoContent) {
             throw Exception("Error al eliminar el dividendo: ${response.status}")
        }
    }
}