package com.frontend.finanzasdashfront.request

import kotlinx.serialization.Serializable

@Serializable
class AddStockRequestDto(
    val stockName: String,
    val brokerId: Long
)