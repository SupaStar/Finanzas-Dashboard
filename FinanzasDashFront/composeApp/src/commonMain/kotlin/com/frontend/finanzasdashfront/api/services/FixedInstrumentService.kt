package com.frontend.finanzasdashfront.api.services

import com.frontend.finanzasdashfront.api.Constants
import com.frontend.finanzasdashfront.dto.fixed_instrument.FixedInstrumentResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class FixedInstrumentService(private val client: HttpClient) {
    private val urlPrefix = "/fixed-instrument"

    suspend fun getAll(): List<FixedInstrumentResponseDto> {
        val response = client.get("${Constants.BaseUrl}${urlPrefix}") {
            contentType(ContentType.Application.Json)
        }
        
        if (response.status == HttpStatusCode.OK) {
            return response.body<List<FixedInstrumentResponseDto>>()
        } else {
            throw Exception("Error al obtener instrumentos fijos: ${response.status}")
        }
    }
}
