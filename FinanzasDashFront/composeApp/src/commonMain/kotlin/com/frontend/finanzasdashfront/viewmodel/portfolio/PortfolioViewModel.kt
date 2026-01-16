package com.frontend.finanzasdashfront.viewmodel.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.DividendService
import com.frontend.finanzasdashfront.api.services.OperationService
import com.frontend.finanzasdashfront.model.portfolio.PortfolioDetailUiState
import com.frontend.finanzasdashfront.utils.year
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val idPortfolio: Long,
    private val operationService: OperationService,
    private val dividendService: DividendService
) : ViewModel() {

    private val _uiState = MutableStateFlow(PortfolioDetailUiState(isLoading = true, portfolioid = idPortfolio))
    val uiState: StateFlow<PortfolioDetailUiState> = _uiState.asStateFlow()

    init {
        loadPortfolioData()
    }

    fun loadPortfolioData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val operationsDeferred = async { operationService.getAllOperations(idPortfolio) }
                val dividendsDeferred = async { dividendService.getDividends(idPortfolio) }

                val operationsResponse = operationsDeferred.await()
                val dividendsResponse = dividendsDeferred.await()

                val opsData = operationsResponse.message
                val divData = dividendsResponse.message

                val yearsDividends = divData.dividends.map { it.year().toString() }
                    .distinct()
                    .sortedDescending()

                if (opsData != null && divData != null) {
                    _uiState.update {
                        it.copy(
                            stockName = opsData.stock.symbol,
                            operations = opsData.operations,
                            dividends = divData.dividends,
                            isLoading = false,
                            yearsDividends = yearsDividends,
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Datos no encontrados") }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Error desconocido")
                }
            }
        }
    }

    fun onTabIndexChanged(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun onYearSelectedChanged(year: String) {
        _uiState.update { it.copy(yearDividendsSelected = year) }
    }
}