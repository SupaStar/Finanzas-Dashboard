package com.frontend.finanzasdashfront.viewmodel.dividend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.AppModule
import com.frontend.finanzasdashfront.dto.dividend.DividendCalendarResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.frontend.finanzasdashfront.getCurrentDateParams

data class DividendCalendarUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedMonth: Int = 1,
    val selectedYear: Int = 2024,
    val data: DividendCalendarResponseDto? = null
)

class DividendCalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DividendCalendarUiState())
    val uiState: StateFlow<DividendCalendarUiState> = _uiState.asStateFlow()

    init {
        val (month, year) = getCurrentDateParams()
        _uiState.update { 
            it.copy(
                selectedMonth = month,
                selectedYear = year
            )
        }
        loadData()
    }

    fun loadData() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val response = AppModule.dividendService.getDividendCalendar(state.selectedMonth, state.selectedYear)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        data = response
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun nextMonth() {
        _uiState.update { state ->
            var newMonth = state.selectedMonth + 1
            var newYear = state.selectedYear
            if (newMonth > 12) {
                newMonth = 1
                newYear++
            }
            state.copy(selectedMonth = newMonth, selectedYear = newYear)
        }
        loadData()
    }

    fun previousMonth() {
        _uiState.update { state ->
            var newMonth = state.selectedMonth - 1
            var newYear = state.selectedYear
            if (newMonth < 1) {
                newMonth = 12
                newYear--
            }
            state.copy(selectedMonth = newMonth, selectedYear = newYear)
        }
        loadData()
    }
}
