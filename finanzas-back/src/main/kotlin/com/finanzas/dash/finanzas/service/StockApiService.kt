package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.client.StockClientConfig
import com.finanzas.dash.finanzas.dto.request.stocks.StockInfoRequestDto
import com.finanzas.dash.finanzas.dto.response.stock.StockResponseDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class StockApiService(private val stockClient: StockClientConfig) {
    fun getStock(stock: String): StockResponseDto {
        return stockClient.stockClient().get()
            .uri("/stock/{stock}", stock)
            .retrieve()
            .bodyToMono(StockResponseDto::class.java)
            .block()
            ?: throw RuntimeException("Error con api")
    }

    fun getStocks(stocks: StockInfoRequestDto): List<StockResponseDto> {
        return stockClient.stockClient().post()
            .uri("/stocks/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(stocks) // WebClient SÍ tiene bodyValue
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<StockResponseDto>>() {})
            .block() // <--- Importante: Espera el resultado y lo convierte en List
            ?: throw RuntimeException("Error con api: respuesta vacía")
    }
}

