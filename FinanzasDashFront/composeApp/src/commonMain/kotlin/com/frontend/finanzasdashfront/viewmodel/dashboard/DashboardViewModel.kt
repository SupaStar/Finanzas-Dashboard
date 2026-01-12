package com.frontend.finanzasdashfront.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.PortfolioService
import com.frontend.finanzasdashfront.config.TokenManager
import com.frontend.finanzasdashfront.model.dashboard.DashboardUiState
import com.frontend.finanzasdashfront.routes.routers.DashboardRouter
import com.frontend.finanzasdashfront.routes.routers.DashboardScreens
import com.frontend.finanzasdashfront.views.dashboard.DashboardScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val tokenManager: TokenManager,
    private val portfolioService: PortfolioService,
    private val router: DashboardRouter
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val response = portfolioService.getPortfolio()
            _uiState.value = _uiState.value.copy(response.message)
            val owo = ""
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