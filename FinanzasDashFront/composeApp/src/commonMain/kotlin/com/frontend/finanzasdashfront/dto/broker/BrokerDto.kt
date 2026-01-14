package com.frontend.finanzasdashfront.dto.broker

import kotlinx.serialization.Serializable

@Serializable
data class BrokerDto(
    var brokerId: Long,
    val name: String,
    val symbol: String
)