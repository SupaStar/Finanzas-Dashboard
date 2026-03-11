package com.frontend.finanzasdashfront

import com.frontend.finanzasdashfront.api.services.AuthService
import com.frontend.finanzasdashfront.api.getEngine
import com.frontend.finanzasdashfront.api.services.BrokerService
import com.frontend.finanzasdashfront.api.services.DividendService
import com.frontend.finanzasdashfront.api.services.OperationService
import com.frontend.finanzasdashfront.api.services.PortfolioService
import com.frontend.finanzasdashfront.api.services.StockService
import com.frontend.finanzasdashfront.api.services.FixedInstrumentService
import com.frontend.finanzasdashfront.api.services.FixedPortfolioService
import com.frontend.finanzasdashfront.api.services.DailyPayService
import com.frontend.finanzasdashfront.config.SecurityManager
import com.frontend.finanzasdashfront.config.TokenManager
import com.frontend.finanzasdashfront.routes.routers.AuthRouter
import com.frontend.finanzasdashfront.routes.routers.DashboardRouter
import com.frontend.finanzasdashfront.viewmodel.auth.LoginViewModel
import com.frontend.finanzasdashfront.viewmodel.auth.RegisterViewModel
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel
import com.frontend.finanzasdashfront.viewmodel.dashboard.stock.SelectStockVM
import com.frontend.finanzasdashfront.viewmodel.portfolio.PortfolioViewModel
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddDividendModalVM
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddOperationModalVM
import com.frontend.finanzasdashfront.viewmodel.portfolio.modal.AddFixedPortfolioModalVM
import com.frontend.finanzasdashfront.viewmodel.portfolio.PortfolioFixedItemVM
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun isDebugBuild(): Boolean

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
        // Logging solo en modo debug
        if (isDebugBuild()) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KTOR_LOG: $message")
                    }
                }
            }
        }
        expectSuccess = false
        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value

                if (statusCode == 403) {
                    println("KTOR_LOG: Acceso prohibido (403). Limpiando token...")
                    tokenManager.clearToken()
                }
            }

            handleResponseExceptionWithRequest { exception, request ->

                println("KTOR_LOG: Error de red o validación: ${exception.message}")
            }
        }
    }


    val tokenManager = TokenManager(SecurityManager())
    val authService = AuthService(httpClient)
    val portfolioService = PortfolioService(httpClient)
    val operationService = OperationService(httpClient)
    val dividendService = DividendService(httpClient)
    val stockService = StockService(httpClient)
    val brokerService = BrokerService(httpClient)
    val fixedInstrumentService = FixedInstrumentService(httpClient)
    val fixedPortfolioService = FixedPortfolioService(httpClient)
    val dailyPayService = DailyPayService(httpClient)
    val dashboardRouter = DashboardRouter()
    val authRouter = AuthRouter()
    // 3. Proveemos el ViewModel
    fun provideLoginViewModel() = LoginViewModel(authService, tokenManager, authRouter)
    fun provideRegisterViewModel() = RegisterViewModel(authService, tokenManager, authRouter)
    fun provideDashboardViewModel() = DashboardViewModel(tokenManager, portfolioService, fixedPortfolioService, dashboardRouter)

    fun provideSelectStockVM() = SelectStockVM(stockService, brokerService, portfolioService)
    fun providePortfolioViewModel(idPortfolio:Long) = PortfolioViewModel(idPortfolio, operationService, dividendService, portfolioService)
    fun provideAddOperationVM() = AddOperationModalVM(operationService)
    fun provideAddDividendVM() = AddDividendModalVM(dividendService)
    fun provideAddFixedPortfolioModalVM() = AddFixedPortfolioModalVM(fixedInstrumentService, fixedPortfolioService)
    fun providePortfolioFixedItemVM(idPortfolio: Long) = PortfolioFixedItemVM(idPortfolio, dailyPayService, fixedPortfolioService)
}