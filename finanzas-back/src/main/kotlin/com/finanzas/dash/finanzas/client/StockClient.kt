package com.finanzas.dash.finanzas.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.http.MediaType
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class StockClientConfig(
    @Value("\${stock_py.service.url}")
    private val stockPyServiceUrl: String
) {

    @Bean
    fun stockClient(): WebClient {
        return WebClient.builder()
            .baseUrl(stockPyServiceUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}