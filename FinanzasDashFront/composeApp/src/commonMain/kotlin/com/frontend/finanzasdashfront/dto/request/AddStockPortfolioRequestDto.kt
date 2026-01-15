package com.frontend.finanzasdashfront.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class AddStockPortfolioRequestDto(
    val stockID: Long
)