package com.finanzas.dash.finanzas.dto.response.broker

data class BrokerAllResponseDto (
    var estado: Boolean = true,
    var message: List<BrokerDto>
)