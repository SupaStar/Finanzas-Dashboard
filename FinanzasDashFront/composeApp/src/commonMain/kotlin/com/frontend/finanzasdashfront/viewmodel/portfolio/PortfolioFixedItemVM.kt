package com.frontend.finanzasdashfront.viewmodel.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.DailyPayService
import com.frontend.finanzasdashfront.api.services.FixedPortfolioService
import com.frontend.finanzasdashfront.dto.daily_pay.DailyPayResponseDto
import com.frontend.finanzasdashfront.dto.fixed_portfolio.FixedPortfolioResponseDto
import com.frontend.finanzasdashfront.dto.fixed_portfolio.FixedPortfolioOperationDto
import com.frontend.finanzasdashfront.dto.request.AddFixedPortfolioOperationDto
import com.frontend.finanzasdashfront.dto.request.UpdateFixedPortfolioAmountDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PortfolioFixedItemUiState(
    val isLoading: Boolean = true,
    val isDeleting: Boolean = false,
    val isAddingOperation: Boolean = false,
    val isUpdatingAmount: Boolean = false,
    val portfolio: FixedPortfolioResponseDto? = null,
    val dailyPays: List<DailyPayResponseDto> = emptyList(),
    val operations: List<FixedPortfolioOperationDto> = emptyList(),
    val errorMessage: String? = null,
    val deleteSuccess: Boolean = false
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

                // Fetch the operations
                val operations = try {
                    fixedPortfolioService.getOperations(portfolioId)
                } catch (e: Exception) {
                    emptyList() // It's okay if it fails or there are none initially
                }

                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        portfolio = portfolio, 
                        dailyPays = dailyPays,
                        operations = operations
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

    fun addOperation(request: AddFixedPortfolioOperationDto) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingOperation = true, errorMessage = null) }
            try {
                val updatedPortfolio = fixedPortfolioService.addOperation(portfolioId, request)
                val operations = fixedPortfolioService.getOperations(portfolioId)
                _uiState.update {
                    it.copy(
                        isAddingOperation = false,
                        portfolio = updatedPortfolio,
                        operations = operations
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isAddingOperation = false,
                        errorMessage = e.message ?: "Error al registrar la operación."
                    )
                }
            }
        }
    }

    fun updateAmount(amount: Float) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingAmount = true, errorMessage = null) }
            try {
                val updatedPortfolio = fixedPortfolioService.updateAmount(portfolioId, UpdateFixedPortfolioAmountDto(amount))
                _uiState.update {
                    it.copy(
                        isUpdatingAmount = false,
                        portfolio = updatedPortfolio
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isUpdatingAmount = false,
                        errorMessage = e.message ?: "Error al actualizar el monto."
                    )
                }
            }
        }
    }

    fun deletePortfolio() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, errorMessage = null) }
            try {
                fixedPortfolioService.delete(portfolioId)
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        deleteSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "Error al eliminar el portafolio fijo."
                    )
                }
            }
        }
    }
}
