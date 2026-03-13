package com.frontend.finanzasdashfront.viewmodel.portfolio

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FibraDividendCalculatorUiState(
    val montoResultado: String = "",
    val montoReembolso: String = "",
    val taxRate: String = "30",
    // Read-only, passed from portfolio
    val numeroTitulos: Float = 0f,
    // Computed — exposed as convenience
    val totalResultado: Float = 0f,
    val totalReembolso: Float = 0f,
    val impuestos: Float = 0f,
    val netoResultado: Float = 0f,
    val netoPago: Float = 0f,
)

class FibraDividendCalculatorVM : ViewModel() {

    private val _uiState = MutableStateFlow(FibraDividendCalculatorUiState())
    val uiState: StateFlow<FibraDividendCalculatorUiState> = _uiState.asStateFlow()

    fun init(numeroTitulos: Float) {
        _uiState.update {
            FibraDividendCalculatorUiState(numeroTitulos = numeroTitulos)
        }
        recalculate()
    }

    fun onMontoResultadoChanged(value: String) {
        _uiState.update { it.copy(montoResultado = value) }
        recalculate()
    }

    fun onMontoReembolsoChanged(value: String) {
        _uiState.update { it.copy(montoReembolso = value) }
        recalculate()
    }

    fun onTaxRateChanged(value: String) {
        _uiState.update { it.copy(taxRate = value) }
        recalculate()
    }

    fun reset() {
        val titulos = _uiState.value.numeroTitulos
        _uiState.update {
            FibraDividendCalculatorUiState(numeroTitulos = titulos)
        }
    }

    private fun recalculate() {
        _uiState.update { s ->
            val titulos = s.numeroTitulos
            val resultado = s.montoResultado.toFloatOrNull() ?: 0f
            val reembolso = s.montoReembolso.toFloatOrNull() ?: 0f
            val tasa = s.taxRate.toFloatOrNull() ?: 0f

            val totalResultado = resultado * titulos
            val totalReembolso = reembolso * titulos
            val impuestos = totalResultado * tasa / 100f
            val netoResultado = totalResultado - impuestos
            val netoPago = netoResultado + totalReembolso

            s.copy(
                totalResultado = totalResultado,
                totalReembolso = totalReembolso,
                impuestos = impuestos,
                netoResultado = netoResultado,
                netoPago = netoPago,
            )
        }
    }
}
