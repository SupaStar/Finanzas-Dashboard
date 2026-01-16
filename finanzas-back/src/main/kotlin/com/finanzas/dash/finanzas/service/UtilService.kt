package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.client.StockClientConfig
import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.response.utils.UsdValueResponseDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UtilService(private val stockClient: StockClientConfig) {
    fun getUsdValue(): UsdValueResponseDto {
        return stockClient.stockClient().get()
            .uri("/USD_MXN")
            .retrieve()
            .onStatus({ it.is4xxClientError }) {
                throw GeneralRequestException(listOf("Error encontrando el precio del dolar"), HttpStatus.NOT_FOUND)
            }
            .onStatus({ it.is5xxServerError }) {
                throw GeneralRequestException(listOf("Error procesando la solicitud"), HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .bodyToMono(UsdValueResponseDto::class.java)
            .block()
            ?: throw GeneralRequestException(listOf("Error encontrando el valor"), HttpStatus.BAD_REQUEST)
    }
}