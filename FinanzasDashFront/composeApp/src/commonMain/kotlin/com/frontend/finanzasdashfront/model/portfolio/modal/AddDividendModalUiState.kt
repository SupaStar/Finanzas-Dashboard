package com.frontend.finanzasdashfront.model.portfolio.modal

data class AddDividendModalUiState(
    val isLoading: Boolean = false,
    val dividendTypeSelectedText: String = "Selecciona tipo de dividendo",
    val dividendTypeValue: String = "",
    val value: String = "",
    val currencyCodeSelected: String = "",
    val currencyCodeSelectedText: String = "Selecciona la moneda",
    val paidDateSelected: String = "",
    val tax: String = "",
    val exchangeRate: String = "1",
    val errorMessage: String = "",
    val isExpandedDividendTypeSelected: Boolean = false,
    val isExpandedCurrencyCodeSelected: Boolean = false,
)