package com.finanzas.dash.finanzas.dto.request.stocks

data class StockHistoricalRequestDto(
    val tickers: List<String>,
    val start: String,
    val end: String
)
