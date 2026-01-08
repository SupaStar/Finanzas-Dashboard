package com.finanzas.dash.finanzas.dto.response.broker

data class BrokerDto(
    var brokerId: Long,
    val name: String,
    val symbol: String
)