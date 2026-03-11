package com.frontend.finanzasdashfront.viewmodel.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.DailyPayService
import com.frontend.finanzasdashfront.api.services.FixedPortfolioService
import com.frontend.finanzasdashfront.dto.daily_pay.DailyPayResponseDto
import com.frontend.finanzasdashfront.dto.fixed_portfolio.FixedPortfolioResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PortfolioFixedItemUiState(
    val isLoading: Boolean = true,
    val portfolio: FixedPortfolioResponseDto? = null,
    val dailyPays: List<DailyPayResponseDto> = emptyList(),
    val errorMessage: String? = null
)

class PortfolioFixedItemVM(
    private val portfolioId: Long,
    private val dailyPayService: DailyPayService,
    private val fixedPortfolioService: FixedPortfolioService
) : ViewModel() {

    private val _uiState = MutableStateFlow(PortfolioFixedItemUiState())
    val uiState: StateFlow<PortfolioFixedItemUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Fetch the specific portfolio by fetching all and filtering
                val allPortfolios = fixedPortfolioService.getAllByUser()
                val portfolio = allPortfolios.find { it.id == portfolioId }
                
                if (portfolio == null) {
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = "No se encontró el portafolio fijo.")
                    }
                    return@launch
                }

                // Fetch the daily pays
                val dailyPays = dailyPayService.getByPortfolio(portfolioId)

                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        portfolio = portfolio, 
                        dailyPays = dailyPays
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = e.message ?: "Error al cargar los datos del portafolio fijo."
                    )
                }
            }
        }
    }
}
