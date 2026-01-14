package com.frontend.finanzasdashfront.viewmodel.portfolio.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.AppModule.operationService
import com.frontend.finanzasdashfront.api.services.OperationService
import com.frontend.finanzasdashfront.model.portfolio.modal.AddOperationModalUiState
import com.frontend.finanzasdashfront.dto.request.AddOperationRequestDto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddOperationModalVM(private val operationService: OperationService) : ViewModel() {
    private val _uiState = MutableStateFlow(AddOperationModalUiState(isLoading = false))
    private val _reloadOperationsEvent = MutableSharedFlow<Unit>()
    private val _closeModalsEvent = MutableSharedFlow<Unit>()
    val uiState: StateFlow<AddOperationModalUiState> = _uiState.asStateFlow()
    val reloadOperationsEvent: SharedFlow<Unit> = _reloadOperationsEvent.asSharedFlow()
    val closeEvent: SharedFlow<Unit> = _closeModalsEvent.asSharedFlow()

    fun onExpandedSelectOperationType(newValue: Boolean) {
        _uiState.update { it.copy(isExpandedSelect = newValue) }
    }

    fun onOperationTypeSelected(operationType: String, operationValue: String) {
        _uiState.update {
            it.copy(
                operationTypeSelectedText = operationType,
                operationTypeValue = operationValue,
                isExpandedSelect = false
            )
        }
    }

    fun onQuantityChange(newValue: String) {
        _uiState.update { it.copy(quantity = newValue) }
    }

    fun onPriceChange(newValue: String) {
        _uiState.update { it.copy(price = newValue) }
    }

    fun onFeeChange(newValue: String) {
        _uiState.update { it.copy(fee = newValue) }
    }

    fun onTaxChange(newValue: String) {
        _uiState.update { it.copy(tax = newValue) }
    }

    fun onDateChange(newValue: String) {
        _uiState.update { it.copy(operationDate = newValue) }
    }

    fun saveOperation(idPortfolio: Long) {
        println("saving operation $idPortfolio")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val request = AddOperationRequestDto(
                    operationType = _uiState.value.operationTypeValue,
                    quantity = _uiState.value.quantity.toDouble(),
                    price = _uiState.value.price.toDouble(),
                    fee = _uiState.value.fee.toDouble(),
                    tax = _uiState.value.tax.toDouble(),
                    portfolioId = idPortfolio,
                    operationDate = _uiState.value.operationDate,
                )
                val response = operationService.addOperation(request)
                if (response.estado) {
                    resetState()
                    _reloadOperationsEvent.emit(Unit)
                    _closeModalsEvent.emit(Unit)
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
                    it.copy(isLoading = false, errorMessage = "Error desconocido")
                }
            }
        }
    }
    fun resetState() {
        _uiState.value = AddOperationModalUiState(isLoading = false)
    }
}