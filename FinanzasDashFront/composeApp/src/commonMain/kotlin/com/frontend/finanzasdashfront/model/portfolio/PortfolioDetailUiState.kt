package com.frontend.finanzasdashfront.model.portfolio

import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.dto.operation.OperationDto
import com.frontend.finanzasdashfront.dto.portfolio.PortfolioGeneralInformationDto

data class PortfolioDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val stockName: String = "",
    val stockCurrency: String = "",
    val portfolioid: Long = 0,
    val operations: List<OperationDto> = emptyList(),
    val dividends: List<DividendDto> = emptyList(),
    val generalInformation: List<PortfolioGeneralInformationDto> = emptyList(),
    val optionsTabs: List<String> = listOf("Operaciones", "Dividendos", "Info"),
    val selectedTabIndex: Int = 0,
    val selectYearText: String = "Selecciona un año para ver sus detalles",
    val yearsDividends: List<String> = emptyList(),
    val yearDividendsSelected: String = "",
    val isExpandedDividendsYearSelected: Boolean = false,
)