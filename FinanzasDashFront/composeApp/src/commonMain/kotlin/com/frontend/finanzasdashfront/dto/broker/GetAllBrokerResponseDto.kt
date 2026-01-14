package com.frontend.finanzasdashfront.dto.broker

import kotlinx.serialization.Serializable

@Serializable
data class GetAllBrokerResponseDto(
    var estado: Boolean = true,
    var message: List<BrokerDto> = emptyList(),
    val errors: List<String>? = null
)