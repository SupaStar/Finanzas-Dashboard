package com.frontend.finanzasdashfront

import com.frontend.finanzasdashfront.api.services.AuthService
import com.frontend.finanzasdashfront.api.getEngine
import com.frontend.finanzasdashfront.api.services.DividendService
import com.frontend.finanzasdashfront.api.services.OperationService
import com.frontend.finanzasdashfront.api.services.PortfolioService
import com.frontend.finanzasdashfront.config.SecurityManager
import com.frontend.finanzasdashfront.config.TokenManager
import com.frontend.finanzasdashfront.routes.routers.DashboardRouter
import com.frontend.finanzasdashfront.viewmodel.auth.LoginViewModel
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel
import com.frontend.finanzasdashfront.viewmodel.portfolio.PortfolioViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AppModule {
    private val httpClient = HttpClient(getEngine()) { // getEngine() es el expect/actual de motores
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
        install(DefaultRequest) {
            val token = tokenManager.getToken()
            if (token != null && !url.encodedPath.contains("/login")) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR_LOG: $message") // Verás esto en el Logcat o consola
                }
            }
        }
    }


    val tokenManager = TokenManager(SecurityManager())
    val authService = AuthService(httpClient)
    val portfolioService = PortfolioService(httpClient)
    val operationService = OperationService(httpClient)
    val dividendService = DividendService(httpClient)
    val dashboardRouter = DashboardRouter()
    // 3. Proveemos el ViewModel
    fun provideLoginViewModel() = LoginViewModel(authService, tokenManager)
    fun provideDashboardViewModel() = DashboardViewModel(tokenManager, portfolioService, dashboardRouter)

    fun providePortfolioViewModel(idPortfolio:Long) = PortfolioViewModel(idPortfolio, operationService, dividendService)
}