package com.frontend.finanzasdashfront.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class AddOperationRequestDto(
    val operationType: String,
    val quantity: Double,
    val price: Double,
    val fee: Double,
    val tax: Double,
    val portfolioId: Long,
    val operationDate: String,
)