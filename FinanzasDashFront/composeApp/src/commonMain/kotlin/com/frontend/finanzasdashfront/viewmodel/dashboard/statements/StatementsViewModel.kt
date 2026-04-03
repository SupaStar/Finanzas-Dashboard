package com.frontend.finanzasdashfront.viewmodel.dashboard.statements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.StatementService
import com.frontend.finanzasdashfront.dto.response.statement.StatementResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StatementsUiState(
    val isLoading: Boolean = true,
    val statements: List<StatementResponseDto> = emptyList(),
    val errorMessage: String? = null
)

class StatementsViewModel(
    private val statementService: StatementService
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatementsUiState())
    val uiState: StateFlow<StatementsUiState> = _uiState.asStateFlow()

    init {
        loadStatements()
    }

    fun loadStatements() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = statementService.getUserStatements()
                _uiState.update { it.copy(isLoading = false, statements = response) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Error desconocido") }
            }
        }
    }
}
