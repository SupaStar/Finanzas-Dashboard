package com.frontend.finanzasdashfront.viewmodel.portfolio.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.FixedInstrumentService
import com.frontend.finanzasdashfront.api.services.FixedPortfolioService
import com.frontend.finanzasdashfront.dto.fixed_instrument.FixedInstrumentResponseDto
import com.frontend.finanzasdashfront.dto.request.CreateFixedPortfolioRequestDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddFixedPortfolioUiState(
    val isLoading: Boolean = false,
    val instruments: List<FixedInstrumentResponseDto> = emptyList(),
    val selectedInstrument: FixedInstrumentResponseDto? = null,
    val amount: String = "",
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class AddFixedPortfolioModalVM(
    private val fixedInstrumentService: FixedInstrumentService,
    private val fixedPortfolioService: FixedPortfolioService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFixedPortfolioUiState())
    val uiState: StateFlow<AddFixedPortfolioUiState> = _uiState.asStateFlow()

    init {
        loadInstruments()
    }

    private fun loadInstruments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val data = fixedInstrumentService.getAll()
                _uiState.update { it.copy(isLoading = false, instruments = data) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = e.message ?: "Error al cargar instrumentos")
                }
            }
        }
    }

    fun selectInstrument(instrument: FixedInstrumentResponseDto) {
        _uiState.update { it.copy(selectedInstrument = instrument) }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun savePortfolio() {
        val instrument = _uiState.value.selectedInstrument
        val amountStr = _uiState.value.amount
        val amountDouble = amountStr.toDoubleOrNull()

        if (instrument == null) {
            _uiState.update { it.copy(errorMessage = "Selecciona un instrumento") }
            return
        }
        if (amountDouble == null || amountDouble <= 0) {
            _uiState.update { it.copy(errorMessage = "Ingresa un monto válido") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = CreateFixedPortfolioRequestDto(instrument.id, amountDouble)
                fixedPortfolioService.create(request)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = e.message ?: "Error al guardar el portafolio")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
