package com.frontend.finanzasdashfront.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontend.finanzasdashfront.api.services.PortfolioService
import com.frontend.finanzasdashfront.config.TokenManager
import kotlinx.coroutines.launch

class DashboardViewModel(private val tokenManager: TokenManager, private val portfolioService: PortfolioService) :
    ViewModel() {

    init {
        viewModelScope.launch {
            val response = portfolioService.getPortfolio()
            val owo = ""
        }
    }
    fun logout() {
        tokenManager.clearToken()
    }
}