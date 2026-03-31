package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.client.StockClientConfig
import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.stocks.StockInfoRequestDto
import com.finanzas.dash.finanzas.dto.request.stocks.StockHistoricalRequestDto
import com.finanzas.dash.finanzas.dto.response.stock.StockApiResponseDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service


@Service
class StockApiService(private val stockClient: StockClientConfig) {
    fun getStock(stock: String): StockApiResponseDto {
        return stockClient.stockClient().get()
            .uri("/stock/{stock}", stock)
            .retrieve()
            .onStatus({ it.is4xxClientError }) {
                throw GeneralRequestException(listOf("Error encontrando la accion"), HttpStatus.NOT_FOUND)
            }
            .onStatus({ it.is5xxServerError }) {
                throw GeneralRequestException(listOf("Error procesando la accion"), HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .bodyToMono(StockApiResponseDto::class.java)
            .block()
            ?: throw GeneralRequestException(listOf("Error encontrando las acciones"), HttpStatus.BAD_REQUEST)
    }

    fun getStocks(stocks: StockInfoRequestDto): List<StockApiResponseDto> {
        return stockClient.stockClient().post()
            .uri("/stocks/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(stocks) // WebClient SÍ tiene bodyValue
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<StockApiResponseDto>>() {})
            .block() // <--- Importante: Espera el resultado y lo convierte en List
            ?: throw GeneralRequestException(listOf("Error encontrando las acciones"), HttpStatus.BAD_REQUEST)
    }

    fun getHistoricalPrices(request: StockHistoricalRequestDto): Map<String, Map<String, Double>> {
        return stockClient.stockClient().post()
            .uri("/historical/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<String, Map<String, Double>>>() {})
            .block() ?: emptyMap()
    }
}

