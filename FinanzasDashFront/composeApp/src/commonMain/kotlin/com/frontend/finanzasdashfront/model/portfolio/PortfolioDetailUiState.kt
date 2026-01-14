package com.frontend.finanzasdashfront.model.portfolio

import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.dto.operation.OperationDto

data class PortfolioDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val stockName: String = "",
    val portfolioid: Long = 0,
    val operations: List<OperationDto> = emptyList(),
    val dividends: List<DividendDto> = emptyList(),
)