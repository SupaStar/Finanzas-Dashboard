package com.frontend.finanzasdashfront.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import com.frontend.finanzasdashfront.config.TokenManager

class DashboardViewModel(private val tokenManager: TokenManager) : ViewModel(){
    fun logout(){
        tokenManager.clearToken()
    }
}