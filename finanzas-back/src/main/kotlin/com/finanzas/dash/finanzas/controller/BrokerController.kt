package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.response.broker.BrokerAllResponseDto
import com.finanzas.dash.finanzas.service.BrokerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/broker")
class BrokerController(private val brokerService: BrokerService) {

    @GetMapping("/all")
    fun getAll(): BrokerAllResponseDto {
        return brokerService.getAll()
    }
}