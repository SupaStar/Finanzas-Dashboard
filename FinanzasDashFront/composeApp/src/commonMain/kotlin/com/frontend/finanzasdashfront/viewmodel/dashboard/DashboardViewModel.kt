package com.frontend.finanzasdashfront.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.PortfolioService
import com.frontend.finanzasdashfront.config.TokenManager
import com.frontend.finanzasdashfront.model.dashboard.DashboardUiState
import com.frontend.finanzasdashfront.model.dashboard.DataPieChart
import com.frontend.finanzasdashfront.model.dashboard.DataPieChartDashboard
import com.frontend.finanzasdashfront.routes.routers.DashboardRouter
import com.frontend.finanzasdashfront.routes.routers.DashboardScreens
import io.github.koalaplot.core.util.generateHueColorPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val tokenManager: TokenManager,
    private val portfolioService: PortfolioService,
    private val router: DashboardRouter
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = portfolioService.getPortfolio()
                val items = response.message
                val totalValue = items.sumOf { it.Stock.closeDay.toDouble() * it.totalQuantity.toDouble() }
                val groupedByCurrency = items.groupBy { it.Stock.currency }.mapValues { entry ->
                    entry.value.sumOf { it.totalQuantity.toDouble() * it.Stock.closeDay.toDouble() }
                }
                val pieChart = groupedByCurrency.map { (currency, total) ->
                    DataPieChart(total.toFloat(), currency)
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        items = items,
                        totalValue = totalValue,
                        errorMessage = null,
                        chartData = DataPieChartDashboard(data = pieChart, colors = generateHueColorPalette(pieChart.size))
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar los portafolios"
                    )
                }
            }
        }
    }

    fun goToDetail(idPortfolio: Long) {
        println("Clicked $idPortfolio")
        router.goTo(DashboardScreens.PortfolioDetail(idPortfolio))
    }

    fun logout() {
        tokenManager.clearToken()
    }
}