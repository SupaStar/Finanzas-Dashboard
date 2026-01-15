package com.frontend.finanzasdashfront.model.portfolio.modal

data class AddOperationModalUiState(
    val isLoading: Boolean = false,
    val operationTypeSelectedText: String = "Selecciona tipo de operacion",
    val operationTypeValue: String = "",
    val quantity: String = "",
    val price: String = "",
    val fee: String = "",
    val tax: String = "",
    val operationDate: String = "",
    val isExpandedSelect: Boolean = false,
    val errorMessage: String = "",
)