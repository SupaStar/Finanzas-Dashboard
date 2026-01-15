package com.frontend.finanzasdashfront.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class AddDividendPortfolioRequestDto (
    val value: Double,
    val dividendType: String,
    val paidDate: String,
    val currencyCode: String?,
    val tax: Double,
    val exchangeRate: Double,
)