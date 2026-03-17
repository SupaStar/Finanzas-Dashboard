package com.frontend.finanzasdashfront.viewmodel.portfolio.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.DividendService
import com.frontend.finanzasdashfront.dto.request.AddDividendPortfolioRequestDto
import com.frontend.finanzasdashfront.model.portfolio.modal.AddDividendModalUiState
import com.frontend.finanzasdashfront.utils.toLabel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddDividendModalVM(private val dividendService: DividendService) : ViewModel() {
    private val _uiState = MutableStateFlow(AddDividendModalUiState(isLoading = false))
    private val _reloadDividendsEvent = MutableSharedFlow<Unit>()
    private val _closeModalEvent = MutableSharedFlow<Unit>()
    val uiState: StateFlow<AddDividendModalUiState> = _uiState.asStateFlow()
    val closeModalEvent: SharedFlow<Unit> = _closeModalEvent.asSharedFlow()
    val reloadDividendsEvent: SharedFlow<Unit> = _reloadDividendsEvent.asSharedFlow()


    fun initModal() {
        _uiState.update {
            it.copy(
                isEditMode = false,
                dividendId = null
            )
        }
    }

    fun initModalForEdit(dividend: com.frontend.finanzasdashfront.dto.dividend.DividendDto) {
        _uiState.update {
            it.copy(
                isEditMode = true,
                dividendId = dividend.dividendId,
                dividendTypeSelectedText = dividend.dividendType.toLabel(),
                dividendTypeValue = dividend.dividendType.name.lowercase(),
                value = dividend.value.toString(),
                currencyCodeSelected = dividend.currencyCode,
                currencyCodeSelectedText = dividend.currencyCode, // Assuming it's the same
                paidDateSelected = dividend.paidDate,
                tax = dividend.tax.toString(),
                exchangeRate = dividend.exchangeRate.toString(),
            )
        }
    }
    
    fun saveDividend(idPortfolio: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val request = AddDividendPortfolioRequestDto(
                    value = uiState.value.value.toDouble(),
                    dividendType = uiState.value.dividendTypeValue,
                    paidDate = uiState.value.paidDateSelected,
                    currencyCode = uiState.value.currencyCodeSelected,
                    tax = uiState.value.tax.toDouble(),
                    exchangeRate = uiState.value.exchangeRate.toDouble()
                )
                
                val response = if (_uiState.value.isEditMode && _uiState.value.dividendId != null) {
                    dividendService.editDividend(_uiState.value.dividendId!!, request)
                } else {
                    dividendService.addDividend(idPortfolio, request)
                }

                if (response.estado) {
                    resetState()
                    _reloadDividendsEvent.emit(Unit)
                    _closeModalEvent.emit(Unit)
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

    fun onExpandedDividendTypeChange(newValue: Boolean) {
        _uiState.update { it.copy(isExpandedDividendTypeSelected = newValue) }
    }

    fun onExpandedCurrencyChange(newValue: Boolean) {
        _uiState.update { it.copy(isExpandedCurrencyCodeSelected = newValue) }
    }

    fun onDividendTypeChange(dividendType: String, dividendValue: String) {
        _uiState.update {
            val newState = it.copy(
                isExpandedDividendTypeSelected = false,
                dividendTypeSelectedText = dividendType,
                dividendTypeValue = dividendValue
            )
            calculateTaxIfApplicable(newState)
        }
    }

    fun onCurrencyTypeChange(currencyType: String, currencyValue: String) {
        _uiState.update {
            val newState = it.copy(
                isExpandedCurrencyCodeSelected = false,
                currencyCodeSelectedText = currencyType,
                currencyCodeSelected = currencyValue
            )
            calculateTaxIfApplicable(newState)
        }
    }

    fun onValueChange(newValue: String) {
        _uiState.update {
            val newState = it.copy(value = newValue)
            calculateTaxIfApplicable(newState)
        }
    }

    private fun calculateTaxIfApplicable(state: AddDividendModalUiState): AddDividendModalUiState {
        if (state.dividendTypeValue.lowercase() == "cash" && state.currencyCodeSelected.uppercase() == "MXN") {
            val valueDouble = state.value.toDoubleOrNull() ?: 0.0
            val taxValue = valueDouble * 0.30
            if (valueDouble > 0) {
                return state.copy(tax = taxValue.toString())
            }
        }
        return state
    }

    fun onDateChange(newValue: String) {
        _uiState.update { it.copy(paidDateSelected = newValue) }
    }

    fun onTaxChanged(newValue: String) {
        _uiState.update { it.copy(tax = newValue) }
    }

    fun onExchangeRateChanged(newValue: String) {
        _uiState.update { it.copy(exchangeRate = newValue) }
    }

    fun resetState(){
        _uiState.value = AddDividendModalUiState(isLoading = false)
    }
}