package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.daily_pay.DailyPayResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class DailyPayService(private val client: HttpClient) {
    private val urlPrefix = "/daily-pay"

    suspend fun getByPortfolio(portfolioId: Long): List<DailyPayResponseDto> {
        val response = client.get("${Constants.BaseUrl}${urlPrefix}/portfolio/$portfolioId") {
            contentType(ContentType.Application.Json)
        }
        
        if (response.status == HttpStatusCode.OK) {
            return response.body<List<DailyPayResponseDto>>()
        } else {
            throw Exception("Error al obtener pagos diarios: ${response.status}")
        }
    }
}
