package com.frontend.finanzasdashfront.viewmodel.dashboard.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.BrokerService
import com.frontend.finanzasdashfront.api.services.PortfolioService
import com.frontend.finanzasdashfront.api.services.StockService
import com.frontend.finanzasdashfront.model.dashboard.stock.SelectStockUiState
import com.frontend.finanzasdashfront.request.AddStockPortfolioRequestDto
import com.frontend.finanzasdashfront.request.AddStockRequestDto
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SelectStockVM(
    private val stockService: StockService,
    private val brokerService: BrokerService,
    private val portfolioService: PortfolioService
) : ViewModel() {
    private val _uiState = MutableStateFlow(SelectStockUiState(isLoading = true))
    val uiState: StateFlow<SelectStockUiState> = _uiState.asStateFlow()
    private val _closeEvent = MutableSharedFlow<Unit>()
    val closeEvent = _closeEvent.asSharedFlow()

    init {
        loadStocksAvailable()
    }

    fun loadStocksAvailable() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val stocksDefered = async { stockService.getStocks() }
                val brokerDefered = async { brokerService.getBrokers() }

                val stocksRasponse = stocksDefered.await()
                val brokerResponse = brokerDefered.await()

                if (stocksRasponse != null && brokerResponse != null) {
                    val stocks = stocksRasponse.message
                    val brokers = brokerResponse.message
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            stocks = stocks,
                            brokers = brokers
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Datos no encontrados") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun saveStock(stockName: String, brokerId: Long) {
        if (stockName.isBlank() || brokerId == -1L) {
            _uiState.update { it.copy(errorMessage = "Datos invalidos o vacios") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val response = stockService.addStock(AddStockRequestDto(stockName, brokerId))

                if (response.estado) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                    loadStocksAvailable()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.errors?.joinToString("\n") ?: "Error al guardar"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error inesperado al conectar con el servidor"
                    )
                }
            }
        }
    }

    fun selectStock(stockId: Long) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val request = AddStockPortfolioRequestDto(stockId)
                val response = portfolioService.addStockToPortfolio(request)
                if (response.estado) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                    _closeEvent.emit(Unit)
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.errors?.joinToString("\n") ?: "Error al conectar con el servidor"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
        println("Selecting stock $stockId")
    }

    fun onStockNameChange(newName: String) {
        _uiState.update { it.copy(stockName = newName) }
    }

    fun onBrokerSelected(id: Long, name: String) {
        _uiState.update {
            it.copy(
                selectedOptionId = id,
                selectedOptionText = name,
                expanded = false
            )
        }
    }

    fun onExpanded(expanded: Boolean) {
        _uiState.update { it.copy(expanded = expanded) }
    }

    fun onStockSelected(id: Long) {
        _uiState.update { it.copy(selectedOptionId = id) }
        selectStock(stockId = id)
    }
}