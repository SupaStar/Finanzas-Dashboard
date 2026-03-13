package com.frontend.finanzasdashfront.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.PortfolioService
import com.frontend.finanzasdashfront.api.services.FixedPortfolioService
import com.frontend.finanzasdashfront.config.TokenManager
import com.frontend.finanzasdashfront.dto.charts.DataPieChart
import com.frontend.finanzasdashfront.dto.charts.DataPieChartDashboard
import com.frontend.finanzasdashfront.model.dashboard.DashboardUiState
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
    private val fixedPortfolioService: FixedPortfolioService,
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
                val usdExchangeRate = response.usdPrice.toDouble()
                
                // Fetch fixed portfolios
                val fixedPortfolios = try {
                    fixedPortfolioService.getAllByUser()
                } catch (e: Exception) {
                    emptyList() // Fallback if it fails, or handle properly
                }

                val totalValueStocks = items.sumOf { item ->
                    val stock = item.Stock
                    val subtotal = stock.closeDay.toDouble() * item.totalQuantity.toDouble()

                    if (stock.currency != "MXN") subtotal * usdExchangeRate else subtotal
                }
                
                val totalValueFixed = fixedPortfolios.sumOf { it.amount }
                
                val groupedByCurrency = items.groupBy { it.Stock.currency }.mapValues { entry ->
                    entry.value.sumOf { it.totalQuantity.toDouble() * it.Stock.closeDay.toDouble() }
                }
                val pieChartItems = groupedByCurrency.map { (currency, total) ->
                    DataPieChart(total.toFloat(), currency)
                }.toMutableList()
                // Add fixed portfolio as its own slice if non-zero
                if (totalValueFixed > 0.0) {
                    pieChartItems.add(DataPieChart(totalValueFixed.toFloat(), "Fija"))
                }
                val pieChart = pieChartItems.toList()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        items = items,
                        fixedPortfolios = fixedPortfolios,
                        totalValue = totalValueStocks,
                        totalValueFixed = totalValueFixed,
                        errorMessage = null,
                        usdValue = response.usdPrice,
                        chartData = DataPieChartDashboard(
                            data = pieChart,
                            colors = generateHueColorPalette(pieChart.size)
                        )
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

    fun goToFixedDetail(idPortfolio: Long) {
        println("Clicked fixed $idPortfolio")
        router.goTo(DashboardScreens.FixedPortfolioDetail(idPortfolio))
    }

    fun logout() {
        tokenManager.clearToken()
    }

    fun onFilterChanged(newValue: String) {
        var filteredData = _uiState.value.items
        if (newValue.isNotEmpty()) {
            filteredData = _uiState.value.items.filter { it.Stock.symbol.contains(newValue, true) }
        }
        _uiState.update { it.copy(filterStock = newValue, filteredStocks = filteredData) }
    }

    fun onTabIndexChanged(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun toggleUsdDisplay() {
        _uiState.update { it.copy(showUsdAsMxn = !it.showUsdAsMxn) }
    }
}