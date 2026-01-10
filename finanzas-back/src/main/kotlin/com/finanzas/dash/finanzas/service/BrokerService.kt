package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.dto.response.broker.BrokerAllResponseDto
import com.finanzas.dash.finanzas.repository.BrokerRepository
import com.finanzas.dash.finanzas.utils.extension.toDto
import org.springframework.stereotype.Service

@Service
class BrokerService(private val brokerRepository: BrokerRepository) {
    fun getAll(): BrokerAllResponseDto {
        val brokers = brokerRepository.findAll()
        return BrokerAllResponseDto(message = brokers.map { it.toDto() })
    }
}