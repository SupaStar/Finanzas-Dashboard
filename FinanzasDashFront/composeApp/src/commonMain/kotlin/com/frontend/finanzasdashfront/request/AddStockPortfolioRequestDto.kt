package com.frontend.finanzasdashfront.request

import kotlinx.serialization.Serializable

@Serializable
data class AddStockPortfolioRequestDto(
    val stockID: String
)