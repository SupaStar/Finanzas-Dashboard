package com.frontend.finanzasdashfront.dto.request

import kotlinx.serialization.Serializable

@Serializable
class AddStockRequestDto(
    val stockName: String,
    val brokerId: Long
)