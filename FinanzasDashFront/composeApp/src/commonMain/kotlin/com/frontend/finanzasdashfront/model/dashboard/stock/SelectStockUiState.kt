package com.frontend.finanzasdashfront.model.dashboard.stock

import com.frontend.finanzasdashfront.dto.broker.BrokerDto
import com.frontend.finanzasdashfront.dto.stock.StockDto

data class SelectStockUiState(
    val isLoading: Boolean = false,
    val stocks: List<StockDto> = emptyList(),
    val errorMessage: String? = null,
    val brokers: List<BrokerDto> = emptyList(),
    val selectedOptionText: String = "Selecciona un Broker",
    val selectedOptionId: Long = -1L,
    val stockName: String = "",
    val expanded: Boolean = false
)